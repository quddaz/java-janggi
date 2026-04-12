package repository;

import domain.place.Place;
import domain.position.Position;
import dto.BoardRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardDao {

    private static final String DELETE_SQL =
            "DELETE FROM board_piece WHERE game_room_id = ?";

    private static final String INSERT_SQL =
            "INSERT INTO board_piece (game_room_id, position_row, position_col, side, type) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_SQL =
            "SELECT position_row, position_col, side, type FROM board_piece WHERE game_room_id = ?";

    public void deleteByRoomId(Connection conn, long roomId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, roomId);
            stmt.executeUpdate();
        }
    }

    public void insertBoard(Connection conn, long roomId, Map<Position, Place> board) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            for (Map.Entry<Position, Place> entry : board.entrySet()) {
                addBatch(stmt, roomId, entry);
            }
            stmt.executeBatch();
        }
    }

    private void addBatch(PreparedStatement stmt, long roomId, Map.Entry<Position, Place> entry) throws SQLException {
        Place place = entry.getValue();
        if (place.getSide().isEmpty()) {
            return;
        }

        Position pos = entry.getKey();

        stmt.setLong(1, roomId);
        stmt.setInt(2, pos.getRow());
        stmt.setInt(3, pos.getColumn());
        stmt.setString(4, place.getSide().get().getName());
        stmt.setString(5, place.getFormat());

        stmt.addBatch();
    }

    public List<BoardRow> findBoard(Connection conn, long roomId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_SQL)) {
            stmt.setLong(1, roomId);

            try (ResultSet rs = stmt.executeQuery()) {
                return mapRows(rs);
            }
        }
    }

    private List<BoardRow> mapRows(ResultSet rs) throws SQLException {
        List<BoardRow> result = new ArrayList<>();

        while (rs.next()) {
            result.add(new BoardRow(
                    rs.getInt("position_row"),
                    rs.getInt("position_col"),
                    rs.getString("side"),
                    rs.getString("type")
            ));
        }

        return result;
    }
}
