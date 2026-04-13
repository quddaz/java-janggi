package domain.board;

import domain.place.Place;
import domain.position.Position;
import java.sql.Connection;
import java.util.Map;

public interface BoardRepository {
    void saveBoard(long roomId, Map<Position, Place> board, Connection conn);

    Board findBoard(long roomId, Connection conn);
}
