package domain.place.moveStrategy;

import domain.place.PalaceArea;
import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.List;

public class OneStepMoveStrategy implements MoveStrategy {

    private static final List<Direction> ORTHOGONAL_DIRECTIONS = List.of(
            Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.TOP
    );

    @Override
    public List<Position> getPath(Position from, Position to) {
        return ORTHOGONAL_DIRECTIONS.stream()
                .flatMap(direction -> from.moveIfInBounds(direction).stream())
                .filter(PalaceArea::isInsidePalace)
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
