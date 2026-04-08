package domain.place.moveStrategy;

import domain.place.PalaceArea;
import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StraightMoveStrategy implements MoveStrategy {

    @Override
    public List<Position> getPath(Position from, Position target) {

        if (isPalaceMove(from, target)) {
            return getPalacePath(from, target);
        }

        if (!from.isStraightWith(target)) {
            return Collections.emptyList();
        }

        Direction direction = Direction.straight(from, target);
        return createPath(from, target, direction);
    }

    private boolean isPalaceMove(Position from, Position to) {
        return PalaceArea.isInsideSpecialPalace(from) && PalaceArea.isInsideSpecialPalace(to);
    }

    private List<Position> getPalacePath(Position from, Position target) {
        if (!isValidPalaceStraight(from, target)) {
            return Collections.emptyList();
        }

        Direction direction = Direction.straight(from, target);
        return createPalacePath(from, target, direction);
    }

    private boolean isValidPalaceStraight(Position from, Position to) {
        return from.getRow() == to.getRow() || from.getColumn() == to.getColumn();
    }

    private List<Position> createPalacePath(Position from, Position target, Direction direction) {
        List<Position> path = new ArrayList<>();
        Position current = from;

        while (!current.equals(target)) {
            current = current.moveIfInBounds(direction)
                    .filter(PalaceArea::isInsidePalace)
                    .orElseThrow(() -> new IllegalArgumentException("궁성 이동 범위 벗어남"));
            path.add(current);
        }

        return path;
    }

    @Override
    public boolean canMove(List<Place> places, Side movingSide) {
        return isValidPlaceSize(places)
                && !isDestinationBlocked(places, movingSide)
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

    private List<Position> createPath(Position from, Position target, Direction direction) {
        List<Position> path = new ArrayList<>();
        Position current = from;

        while (!current.equals(target)) {
            current = moveOrThrow(current, direction);
            path.add(current);
        }

        return path;
    }

    private Position moveOrThrow(Position current, Direction direction) {
        return current.moveIfInBounds(direction)
                .orElseThrow(() -> new IllegalArgumentException("직선 이동 범위 벗어남"));
    }
}
