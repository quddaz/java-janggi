package repository.impl;

import domain.board.BoardFactory;
import domain.place.Place;
import domain.place.piece.PieceFactory;
import domain.place.piece.Side;
import domain.position.Position;
import dto.BoardRow;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
        try {
            List<BoardRow> rows = boardDao.findBoard(conn, roomId);
            return toBoard(rows);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 조회 실패", e);
        }
    }

    private Map<Position, Place> toBoard(List<BoardRow> rows) {
        Map<Position, Place> board = BoardFactory.setUpEmpty();

        for (BoardRow row : rows) {
            board.put(toPosition(row), toPlace(row));
        }

        return board;
    }

    private Position toPosition(BoardRow row) {
        return new Position(row.row(), row.col());
    }

    private Place toPlace(BoardRow row) {
        Side side = Side.from(row.side());
        return PieceFactory.from(row.type()).createPlace(side);
    }
}
