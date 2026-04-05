package repository;

import dto.GameRoomDto;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GameRoomRepository {
    long save(String name, String side);

    List<GameRoomDto> findAll();

    Optional<GameRoomDto> findById(long id);

    void update(long id, String side);
}
