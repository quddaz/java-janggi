package service;

import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import dto.GameRoomDto;
import java.util.List;
import java.util.Map;

public interface GameService {
    void saveGame(Map<Position, Place> board, String name, Side side);
    void updateGame(Map<Position, Place> board, Side side, long roomId);
    Map<Position, Place> findBoardByRoomId(long roomId);
    GameRoomDto findGameRoomByRoomId(long roomId);
    public List<GameRoomDto> findGameRoomAll();
}
