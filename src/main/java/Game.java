import domain.board.Board;
import domain.place.piece.Side;
import domain.position.Position;

public class Game {

    private final Board board;
    private Side turn;
    private Long roomId;

    public Game(Board board, Side turn, Long roomId) {
        this.board = board;
        this.turn = turn;
        this.roomId = roomId;
    }

    public boolean isFinished() {
        return !board.isAliveGeneral(turn);
    }

    public void nextTurn() {
        turn = turn.opposite();
    }

    public void move(Position from, Position to) {
        board.move(from, to, turn);
    }

    public boolean isCheck() {
        return board.isCheck(turn);
    }

    public double getScore(Side side) {
        return board.getSideScore(side);
    }

    public Board getBoard() {
        return board;
    }

    public Side getTurn() {
        return turn;
    }

    public Long getRoomId() {
        return roomId;
    }
}