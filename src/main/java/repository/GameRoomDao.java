package repository;

import dto.GameRoomRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameRoomDao {

    private static final String INSERT_SQL =
            "INSERT INTO game_room (name, current_turn) VALUES (?,?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, name, current_turn, created_at FROM game_room WHERE id = ?";

    private static final String SELECT_ALL =
            "SELECT id, name, current_turn, created_at FROM game_room";

    private static final String UPDATE_BY_ID =
            "UPDATE game_room SET current_turn = ? WHERE id = ?";

    public long save(Connection conn, String name, String side) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, side);
            stmt.executeUpdate();
            return getGeneratedId(stmt);
        }
    }

    public List<GameRoomRow> findAll(Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            return mapRows(rs);
        }
    }

    public Optional<GameRoomRow> findById(Connection conn, long id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    public void update(Connection conn, long roomId, String side) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_BY_ID)) {
            stmt.setString(1, side);
            stmt.setLong(2, roomId);
            stmt.executeUpdate();
        }
    }

    private List<GameRoomRow> mapRows(ResultSet rs) throws SQLException {
        List<GameRoomRow> result = new ArrayList<>();
        while (rs.next()) {
            result.add(mapRow(rs));
        }
        return result;
    }

    private GameRoomRow mapRow(ResultSet rs) throws SQLException {
        return new GameRoomRow(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("current_turn"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    private long getGeneratedId(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) return rs.getLong(1);
        }
        throw new RuntimeException("[ERROR] ID 생성 실패");
    }
}
