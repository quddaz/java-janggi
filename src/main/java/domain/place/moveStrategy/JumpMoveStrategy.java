package domain.place.moveStrategy;

import domain.place.PalaceArea;
import domain.place.Place;
import domain.place.piece.PieceSymbol;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JumpMoveStrategy implements MoveStrategy {

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
                && !isDestinationBlocked(places, movingSide)
                && !containsInvalidJump(places)
                && hasExactlyOneObstacle(places);
    }

    private boolean isPalaceMove(Position fromPosition, Position targetPosition) {
        return PalaceArea.isInsideSpecialPalace(fromPosition)
                && PalaceArea.isInsideSpecialPalace(targetPosition);
    }

    private List<Position> getPalacePath(Position fromPosition, Position targetPosition) {
        if (!isValidPalaceDiagonal(fromPosition, targetPosition)) {
            return Collections.emptyList();
        }

        Direction direction = Direction.diagonal(fromPosition, targetPosition);
        List<Position> path = new ArrayList<>();

        if (!collectPalacePath(fromPosition, targetPosition, direction, path)) {
            return Collections.emptyList();
        }
        return path;
    }

    private boolean collectPalacePath(Position from, Position target, Direction direction, List<Position> path) {
        Position current = from;

        while (!current.equals(target)) {
            if (!moveAndValidate(current, direction, path)) {
                return false;
            }
            current = move(current, direction).get();
        }

        path.add(current);
        return true;
    }

    private boolean moveAndValidate(Position current, Direction direction, List<Position> path) {
        Optional<Position> next = move(current, direction);
        if (next.isEmpty()) return false;

        Position moved = next.get();
        if (!PalaceArea.isInsidePalace(moved)) return false;

        path.add(moved);
        return true;
    }

    private Optional<Position> move(Position position, Direction direction) {
        return position.moveIfInBounds(direction);
    }

    private boolean isValidPalaceDiagonal(Position fromPosition, Position targetPosition) {
        return Math.abs(fromPosition.getRow() - targetPosition.getRow()) ==
                Math.abs(fromPosition.getColumn() - targetPosition.getColumn());
    }

    private List<Position> collectPath(Position fromPosition, Position targetPosition, Direction direction) {
        List<Position> path = new ArrayList<>();
        Optional<Position> current = fromPosition.moveIfInBounds(direction);

        while (current.isPresent()) {
            Position pos = current.get();
            path.add(pos);
            if (pos.equals(targetPosition)) {
                return path;
            }
            current = pos.moveIfInBounds(direction);
        }
        return Collections.emptyList();
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
