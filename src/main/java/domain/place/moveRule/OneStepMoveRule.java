package domain.place.moveRule;

import domain.place.Place;
import domain.place.piece.Side;
import java.util.List;

public class OneStepMoveRule implements MoveRule{

    @Override
    public boolean canMove(List<Place> places, Side side) {
        return isValidPlaceSize(places)
                && !isDestinationBlocked(places, side);
    }

    private boolean isValidPlaceSize(List<Place> places) {
        return !places.isEmpty();
    }

    private boolean isDestinationBlocked(List<Place> places, Side movingSide) {
        Place destination = getLast(places);
        return destination.hasSide(movingSide);
    }

    private Place getLast(List<Place> places) {
        return places.getLast();
    }
}
