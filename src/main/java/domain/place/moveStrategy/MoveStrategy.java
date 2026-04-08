package domain.place.moveStrategy;

import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.List;

public interface MoveStrategy {

    List<Position> getPath(Position from, Position to);

    boolean canMove(List<Place> places, Side fromSide);

}
