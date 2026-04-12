package repository.impl;

import domain.place.piece.Side;
import dto.GameRoomDto;
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
        try (ResultSet rs = gameRoomDao.findAll(conn)) {
            return toList(rs);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 조회 실패", e);
        }
    }

    @Override
    public Optional<GameRoomDto> findById(long id, Connection conn) {
        try (ResultSet rs = gameRoomDao.findById(conn, id)) {
            if (rs.next()) {
                return Optional.of(toGameRoom(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 조회 실패", e);
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

    private List<GameRoomDto> toList(ResultSet rs) throws SQLException {
        List<GameRoomDto> result = new ArrayList<>();
        while (rs.next()) {
            result.add(toGameRoom(rs));
        }
        return result;
    }

    private GameRoomDto toGameRoom(ResultSet rs) throws SQLException {
        return GameRoomDto.of(
                rs.getLong("id"),
                rs.getString("name"),
                Side.from(rs.getString("current_turn")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
