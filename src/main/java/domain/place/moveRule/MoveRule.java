package domain.place.moveRule;

import domain.place.Place;
import domain.place.piece.Side;
import java.util.List;

public interface MoveRule {
    boolean canMove(List<Place> places, Side side);
}