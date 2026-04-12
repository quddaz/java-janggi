package repository.impl;

import domain.board.BoardFactory;
import domain.place.Place;
import domain.place.piece.PieceFactory;
import domain.place.piece.Side;
import domain.position.Position;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import repository.BoardDao;
import repository.BoardRepository;

public class BoardRepositoryImpl implements BoardRepository {

    private final BoardDao boardDao = new BoardDao();

    @Override
    public void saveBoard(long roomId, Map<Position, Place> board, Connection conn) {
        try {
            boardDao.deleteByRoomId(conn, roomId);
            boardDao.insertBoard(conn, roomId, board);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 저장 실패", e);
        }
    }

    @Override
    public Map<Position, Place> findBoard(long roomId, Connection conn) {
        try (ResultSet rs = boardDao.findBoard(conn, roomId)) {
            return toBoard(rs);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 조회 실패", e);
        }
    }

    private Map<Position, Place> toBoard(ResultSet rs) throws SQLException {
        Map<Position, Place> board = BoardFactory.setUpEmpty();

        while (rs.next()) {
            board.put(toPosition(rs), toPlace(rs));
        }

        return board;
    }

    private Position toPosition(ResultSet rs) throws SQLException {
        return new Position(
                rs.getInt("position_row"),
                rs.getInt("position_col")
        );
    }

    private Place toPlace(ResultSet rs) throws SQLException {
        Side side = Side.from(rs.getString("side"));
        return PieceFactory.from(rs.getString("type")).createPlace(side);
    }
}