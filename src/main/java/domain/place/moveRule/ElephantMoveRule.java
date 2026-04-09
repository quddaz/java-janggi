package domain.place.moveRule;

import domain.place.Place;
import domain.place.piece.Side;
import java.util.List;

public class ElephantMoveRule implements MoveRule{

    @Override
    public boolean canMove(List<Place> places, Side side) {
        if (!isValidPlaceSize(places)) {
            return false;
        }

        return !isBlockedAtFirstStep(places)
                && !isBlockedAtSecondStep(places)
                && !isDestinationBlocked(places, side);
    }

    private boolean isValidPlaceSize(List<Place> places) {
        return places.size() == 3;
    }

    private boolean isBlockedAtFirstStep(List<Place> places) {
        return !places.getFirst().isEmpty();
    }

    private boolean isBlockedAtSecondStep(List<Place> places) {
        return !places.get(1).isEmpty();
    }

    private boolean isDestinationBlocked(List<Place> places, Side fromSide) {
        return places.get(2).hasSide(fromSide);
    }
}
