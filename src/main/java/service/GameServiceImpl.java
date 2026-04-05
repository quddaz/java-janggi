package service;

import config.transactional.Transactional;
import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import dto.GameRoomDto;
import java.util.List;
import java.util.Map;
import repository.BoardRepository;
import repository.GameRoomRepository;

public class GameServiceImpl implements GameService{

    private final BoardRepository boardRepository;
    private final GameRoomRepository gameRoomRepository;


    public GameServiceImpl(BoardRepository boardRepository,
                           GameRoomRepository gameRoomRepository) {
        this.boardRepository = boardRepository;
        this.gameRoomRepository = gameRoomRepository;
    }

    @Transactional
    public void saveGame(Map<Position, Place> board, String name, Side side) {
        long roomId = gameRoomRepository.save(name, side.getName());
        boardRepository.saveBoard(roomId, board);
    }

    @Transactional
    public void updateGame(Map<Position, Place> board, Side side, long roomId) {
        gameRoomRepository.update(roomId, side.getName());
        boardRepository.saveBoard(roomId, board);
    }

    @Transactional
    public Map<Position, Place> findBoardByRoomId(long roomId) {
        findGameRoomByRoomId(roomId);
        return boardRepository.findBoard(roomId);
    }

    @Transactional
    public GameRoomDto findGameRoomByRoomId(long roomId) {
        return gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 방은 없습니다."));
    }

    @Transactional
    public List<GameRoomDto> findGameRoomAll() {
        return gameRoomRepository.findAll();
    }

}
