package domain.place.moveRule;

import domain.place.Place;
import domain.place.piece.Side;
import java.util.List;

public class HorseRule implements MoveRule{

    @Override
    public boolean canMove(List<Place> places, Side side) {
        if (!isValidPlaceSize(places)) {
            return false;
        }

        return !isBlockedAtFirstStep(places)
                && !isDestinationBlocked(places, side);
    }

    private boolean isValidPlaceSize(List<Place> places) {
        return places != null && places.size() == 2;
    }

    private boolean isBlockedAtFirstStep(List<Place> places) {
        return !places.getFirst().isEmpty();
    }

    private boolean isDestinationBlocked(List<Place> places, Side movingSide) {
        return places.get(1).hasSide(movingSide);
    }

}
