package domain.place.moveStrategy;

import domain.place.PalaceArea;
import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OneStepMoveStrategy implements MoveStrategy {

    @Override
    public List<Position> getPath(Position fromPosition, Position targetPosition) {
        if (isPalaceMove(fromPosition, targetPosition)) {
            return getPalacePath(fromPosition, targetPosition);
        }

        if (!fromPosition.isStraightWith(targetPosition)) {
            return Collections.emptyList();
        }
        Direction direction = Direction.straight(fromPosition, targetPosition);
        return collectPath(fromPosition, targetPosition, direction);
    }

    @Override
    public boolean canMove(List<Place> places, Side movingSide) {
        return isValidPlaceSize(places)
                && !isDestinationBlocked(places, movingSide);
    }

    private List<Position> collectPath(Position from, Position target, Direction direction) {
        Optional<Position> next = from.moveIfInBounds(direction);

        if(next.isPresent() && next.get().equals(target))
            return List.of(next.get());

        return Collections.emptyList();
    }

    private List<Position> getPalacePath(Position from, Position target) {
        if (!canMoveInPalace(from, target)) {
            return Collections.emptyList();
        }

        Position next = moveOneStep(from, target);
        return List.of(next);
    }

    private boolean canMoveInPalace(Position from, Position to) {
        return isPalaceMove(from, to)
                && isValidPalaceDiagonal(from, to);
    }

    private Position moveOneStep(Position from, Position target) {
        Direction direction = Direction.diagonal(from, target);
        return from.moveIfInBounds(direction).get();
    }

    private boolean isPalaceMove(Position from, Position to) {
        return PalaceArea.isInsideSpecialPalace(from)
                && PalaceArea.isInsideSpecialPalace(to);
    }

    private boolean isValidPalaceDiagonal(Position from, Position to) {
        return Math.abs(from.getRow() - to.getRow()) ==
                Math.abs(from.getColumn() - to.getColumn());
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
