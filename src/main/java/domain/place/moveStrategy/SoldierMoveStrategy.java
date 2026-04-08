package domain.place.moveStrategy;

import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.List;

public class SoldierMoveStrategy implements MoveStrategy {

    private final List<Direction> directions;

    public SoldierMoveStrategy(Side side) {
        this.directions = initDirections(side);
    }

    private List<Direction> initDirections(Side side) {
        if (side == Side.CHO) {
            return List.of(Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        }
        return List.of(Direction.TOP, Direction.LEFT, Direction.RIGHT);
    }

    @Override
    public List<Position> getPath(Position from, Position to) {
        return directions.stream()
                .flatMap(direction -> from.moveIfInBounds(direction).stream())
                .filter(to::equals)
                .toList();
    }

    @Override
    public boolean canMove(List<Place> places, Side fromSide) {
        if (places.isEmpty()) {
            return false;
        }
        return !isDestinationBlocked(places, fromSide);
    }

    private boolean isDestinationBlocked(List<Place> places, Side fromSide) {
        Place dest = places.get(places.size() - 1);
        return dest.hasSide(fromSide);
    }
}
