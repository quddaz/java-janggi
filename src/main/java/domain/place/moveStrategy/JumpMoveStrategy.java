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
    public List<Position> getPath(Position from, Position to) {
        if (isPalaceMove(from, to)) {
            return getPalacePath(from, to);
        }

        if (!from.isStraightWith(to)) {
            return Collections.emptyList();
        }

        Direction direction = Direction.straight(from, to);
        return collectPath(from, to, direction);
    }

    @Override
    public boolean canMove(List<Place> places, Side fromSide) {
        if (places.isEmpty()) {
            return false;
        }
        if (isDestinationBlocked(places, fromSide)) {
            return false;
        }
        if (containsInvalidJump(places)) {
            return false;
        }
        return hasExactlyOneObstacle(places);
    }

    private boolean isPalaceMove(Position from, Position to) {
        return PalaceArea.isInsidePalace(from)
                && PalaceArea.isInsidePalace(to);
    }

    private List<Position> getPalacePath(Position from, Position to) {
        if (isInvalidPalaceDiagonal(from, to)) {
            return Collections.emptyList();
        }

        return createPath(from, to);
    }

    private boolean isInvalidPalaceDiagonal(Position from, Position to) {
        return !isValidPalaceDiagonal(from, to);
    }

    private List<Position> createPath(Position from, Position to) {
        List<Position> path = new ArrayList<>();
        Direction direction = Direction.diagonal(from, to);

        Position current = from;

        while (!current.equals(to)) {
            current = moveOrThrow(current, direction);
            path.add(current);
        }

        return path;
    }

    private Position moveOrThrow(Position current, Direction direction) {
        return current.moveIfInBounds(direction)
                .orElseThrow(() -> new IllegalArgumentException("궁 이동 범위 벗어남"));
    }

    private boolean isValidPalaceDiagonal(Position from, Position to) {
        return Math.abs(from.getRow() - to.getRow()) ==
                Math.abs(from.getColumn() - to.getColumn());
    }

    private List<Position> collectPath(Position from, Position to, Direction direction) {
        List<Position> path = new ArrayList<>();
        Optional<Position> current = from.moveIfInBounds(direction);

        while (current.isPresent()) {
            if (addAndCheckReached(path, current.get(), to)) {
                return path;
            }
            current = moveNext(current.get(), direction);
        }

        return Collections.emptyList();
    }

    private boolean addAndCheckReached(List<Position> path, Position pos, Position to) {
        path.add(pos);
        return pos.equals(to);
    }

    private Optional<Position> moveNext(Position pos, Direction direction) {
        return pos.moveIfInBounds(direction);
    }

    private boolean isDestinationBlocked(List<Place> places, Side fromSide) {
        Place dest = getLast(places);
        return dest.hasSide(fromSide) || dest.isSameSymbol(PieceSymbol.CANNON);
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

    private List<Place> getMiddle(List<Place> places) {
        return places.subList(0, places.size() - 1);
    }

    private Place getLast(List<Place> places) {
        return places.get(places.size() - 1);
    }
}