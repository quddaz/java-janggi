package domain.place;

import domain.place.piece.PieceSymbol;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Place {

    boolean isEmpty();

    boolean hasSide(Side side);

    boolean isSameSymbol(PieceSymbol pieceSymbol);

    int getScore();

    String getFormat();

    Optional<Side> getSide();

    List<Position> getPath(Position from, Position to);

    boolean canMove(List<Place> path);


}
