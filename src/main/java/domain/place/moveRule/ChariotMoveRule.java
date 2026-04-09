package domain.place.moveRule;

import domain.place.Place;
import domain.place.piece.Side;
import java.util.List;

public class ChariotMoveRule implements MoveRule{

    @Override
    public boolean canMove(List<Place> places, Side side) {
        return isValidPlaceSize(places)
                && !isDestinationBlocked(places, side)
                && isPathClear(places);
    }

    private boolean isValidPlaceSize(List<Place> places) {
        return !places.isEmpty();
    }

    private boolean isDestinationBlocked(List<Place> places, Side movingSide) {
        return getLast(places).hasSide(movingSide);
    }

    private boolean isPathClear(List<Place> places) {
        return getMiddle(places).stream()
                .allMatch(Place::isEmpty);
    }

    private List<Place> getMiddle(List<Place> places) {
        return places.subList(0, places.size() - 1);
    }

    private Place getLast(List<Place> places) {
        return places.getLast();
    }
}
