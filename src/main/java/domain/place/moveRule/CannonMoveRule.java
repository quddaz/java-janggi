package domain.place.moveRule;

import domain.place.Place;
import domain.place.piece.PieceSymbol;
import domain.place.piece.Side;
import java.util.List;

public class CannonMoveRule implements MoveRule{
    @Override
    public boolean canMove(List<Place> places, Side side) {
        return isValidPlaceSize(places)
                && !isDestinationBlocked(places, side)
                && !containsInvalidJump(places)
                && hasExactlyOneObstacle(places);
    }

    private boolean isDestinationBlocked(List<Place> places, Side movingSide) {
        Place destination = getLast(places);
        return destination.hasSide(movingSide) || destination.isSameSymbol(PieceSymbol.CANNON);
    }

    private boolean containsInvalidJump(List<Place> places) {
        return getMiddle(places).stream()
                .anyMatch(place -> place.isSameSymbol(PieceSymbol.CANNON));
    }

    private boolean hasExactlyOneObstacle(List<Place> places) {
        return getMiddle(places).stream()
                .filter(place -> !place.isEmpty())
                .count() == 1;
    }

    private boolean isValidPlaceSize(List<Place> places) {
        return !places.isEmpty();
    }

    private List<Place> getMiddle(List<Place> places) {
        return places.subList(0, places.size() - 1);
    }

    private Place getLast(List<Place> places) {
        return places.getLast();
    }
}
