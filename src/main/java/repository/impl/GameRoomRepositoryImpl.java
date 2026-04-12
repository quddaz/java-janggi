package repository.impl;

import domain.place.piece.Side;
import dto.GameRoomDto;
import dto.GameRoomRow;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import repository.GameRoomDao;
import repository.GameRoomRepository;

public class GameRoomRepositoryImpl implements GameRoomRepository {

    private final GameRoomDao gameRoomDao = new GameRoomDao();

    @Override
    public long save(String name, String side, Connection conn) {
        try {
            return gameRoomDao.save(conn, name, side);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 저장 실패", e);
        }
    }

    @Override
    public List<GameRoomDto> findAll(Connection conn) {
        try {
            List<GameRoomRow> rows = gameRoomDao.findAll(conn);
            return rows.stream()
                    .map(this::toDto)
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<GameRoomDto> findById(long id, Connection conn) {
        try {
            return gameRoomDao.findById(conn, id)
                    .map(this::toDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(long roomId, String side, Connection conn) {
        try {
            gameRoomDao.update(conn, roomId, side);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 업데이트 실패", e);
        }
    }

    private GameRoomDto toDto(GameRoomRow row) {
        return GameRoomDto.of(
                row.id(),
                row.name(),
                Side.from(row.currentTurn()),
                row.createdAt()
        );
    }

}
