package repository.impl;

import domain.board.Board;
import domain.board.BoardFactory;
import domain.place.Place;
import domain.place.piece.PieceFactory;
import domain.place.piece.Side;
import domain.position.Position;
import dto.BoardRow;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import repository.BoardDao;
import domain.board.BoardRepository;

public class BoardRepositoryImpl implements BoardRepository {

    private final BoardDao boardDao;

    public BoardRepositoryImpl(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    @Override
    public void saveBoard(long roomId, Map<Position, Place> board, Connection conn) {
        try {
            boardDao.deleteByRoomId(conn, roomId);
            boardDao.insert(conn, roomId, toCells(board));
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 저장 실패", e);
        }
    }

    @Override
    public Board findBoard(long roomId, Connection conn) {
        try {
            List<BoardRow> rows = boardDao.findById(conn, roomId);
            return toBoard(rows);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] 조회 실패", e);
        }
    }

    private List<BoardRow> toCells(Map<Position, Place> board){
        return board.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> new BoardRow(
                        e.getKey().getRow(),
                        e.getKey().getColumn(),
                        e.getValue().getSide().toString(),
                        e.getValue().getFormat()))
                .toList();
    }

    private Board toBoard(List<BoardRow> rows) {
        Map<Position, Place> board = BoardFactory.setUpEmpty();

        for (BoardRow row : rows) {
            board.put(toPosition(row), toPlace(row));
        }

        return new Board(board);
    }

    private Position toPosition(BoardRow row) {
        return new Position(row.row(), row.col());
    }

    private Place toPlace(BoardRow row) {
        Side side = Side.from(row.side());
        return PieceFactory.from(row.type()).createPlace(side);
    }
}
