import domain.board.Board;
import domain.board.HorseElephantFormation;
import domain.place.piece.Side;
import domain.position.Position;
import domain.board.BoardFactory;
import dto.GameRoomDto;
import parser.AnswerParser;
import parser.CommandParser;
import parser.NumberParser;
import parser.PositionParser;
import service.GameService;
import util.RetryHandler;
import view.InputView;
import view.OutputView;

public class Janggi {

    private final GameService gameService;
    private Game game;

    public Janggi(GameService gameService) {
        this.gameService = gameService;
    }

    public void run() {
        game = createGame();
        play(game);
        OutputView.printWinner(game.getTurn().opposite());
    }

    private Game createGame() {
        OutputView.printStartMenu();

        boolean answer = RetryHandler.retryInput(() ->
                AnswerParser.parse(InputView.readLine())
        );

        if (answer) {
            return loadGame();
        }
        return newGame();
    }

    private Game loadGame() {
        OutputView.printSaveRoomList(gameService.findGameRoomAll());

        long roomId = readRoomId();

        if (isNewGameRequest(roomId)) {
            return newGame();
        }

        return createLoadedGame(roomId);
    }

    private long readRoomId() {
        return RetryHandler.retryInput(() ->
                NumberParser.parse(InputView.readLine())
        );
    }

    private boolean isNewGameRequest(long roomId) {
        return roomId == 0;
    }

    private Game createLoadedGame(long roomId) {
        GameRoomDto gameRoomDto = gameService.findGameRoomByRoomId(roomId);
        Board board = new Board(gameService.findBoardByRoomId(roomId));

        return new Game(board, gameRoomDto.side(), roomId);
    }

    private Game newGame() {
        HorseElephantFormation cho = getHorseElephantFormation(Side.CHO);
        HorseElephantFormation han = getHorseElephantFormation(Side.HAN);

        Board board = BoardFactory.create(cho, han);

        return new Game(board, Side.CHO, null);
    }

    private void play(Game game) {
        while (!game.isFinished()) {
            processTurn(game);
            game.nextTurn();
        }
    }

    private HorseElephantFormation getHorseElephantFormation(Side side) {
        OutputView.printHorseElephantFormation(side);
        String input = InputView.readLine();
        return HorseElephantFormation.from(input);
    }

    private void processTurn(Game game) {
        Board board = game.getBoard();

        OutputView.printBoard(board.getFormatBoard(), board.getSideBoard());
        printScore(game);

        executeTurn(game);
        printCheckIfNeeded(game);
    }

    private void executeTurn(Game game) {
        while (true) {
            try {
                executeMove(game);
                return;
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void executeMove(Game game) {
        Board board = game.getBoard();

        Position from = getFrom(board);
        Position to = getTo();

        game.move(from, to);
    }

    private Position getFrom(Board board) {
        while (true) {
            OutputView.printPieceMove(game.getTurn());
            String input = InputView.readLine();

            if (CommandParser.parse(input)) {
                save(board);
                continue;
            }

            return PositionParser.parsePosition(input);
        }
    }

    private void save(Board board) {
        OutputView.printGameName();

        if (isNewGame()) {
            saveGame(board);
            return;
        }
        updateGame(board);
    }

    private boolean isNewGame() {
        return game.getRoomId() == null;
    }

    private void saveGame(Board board) {
        String name = InputView.readLine();
        gameService.saveGame(board.board(), name, game.getTurn());
        OutputView.printSaveComplete();
    }

    private void updateGame(Board board) {
        gameService.updateGame(board.board(), game.getTurn(), game.getRoomId());
        OutputView.printUpdateComplete();
    }

    private Position getTo() {
        OutputView.printPositionMove(game.getTurn());
        return PositionParser.parsePosition(InputView.readLine());
    }

    private void printCheckIfNeeded(Game game) {
        if (game.isCheck()) {
            OutputView.printCheck(game.getTurn().opposite());
        }
    }

    private void printScore(Game game) {
        double choScore = game.getScore(Side.CHO);
        double hanScore = game.getScore(Side.HAN);

        OutputView.printScore(Side.CHO, choScore, Side.HAN, hanScore);
    }
}