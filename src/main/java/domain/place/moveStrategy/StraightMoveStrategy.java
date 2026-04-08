package domain.place.moveStrategy;

import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StraightMoveStrategy implements MoveStrategy {

    @Override
    public List<Position> getPath(Position from, Position to) {
        if (!from.isStraightWith(to)) {
            return Collections.emptyList();
        }

        Direction direction = Direction.straight(from, to);
        return createPath(from, to, direction);
    }

    @Override
    public boolean canMove(List<Place> places, Side fromSide) {
        if (places.isEmpty()) {
            return false;
        }
        if (isDestinationBlocked(places, fromSide)) {
            return false;
        }
        return isPathClear(places);
    }

    private List<Position> createPath(Position from, Position to, Direction direction) {
        List<Position> path = new ArrayList<>();
        Position current = from;

        while (!current.equals(to)) {
            current = moveOrThrow(current, direction);
            path.add(current);
        }

        return path;
    }

    private Position moveOrThrow(Position current, Direction direction) {
        return current.moveIfInBounds(direction)
                .orElseThrow(() -> new IllegalArgumentException("직선 이동 범위 벗어남"));
    }

    private boolean isDestinationBlocked(List<Place> places, Side fromSide) {
        Place dest = getLast(places);
        return dest.hasSide(fromSide);
    }

    private boolean isPathClear(List<Place> places) {
        return getMiddle(places).stream()
                .allMatch(Place::isEmpty);
    }

    private List<Place> getMiddle(List<Place> places) {
        return places.subList(0, places.size() - 1);
    }

    private Place getLast(List<Place> places) {
        return places.get(places.size() - 1);
    }
}
