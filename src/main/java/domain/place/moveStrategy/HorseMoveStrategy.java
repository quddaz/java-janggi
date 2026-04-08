package domain.place.moveStrategy;

import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HorseMoveStrategy implements MoveStrategy {

    private static final List<List<Direction>> HORSE_MOVE_SEQUENCES = List.of(
            List.of(Direction.TOP, Direction.LEFT_TOP),
            List.of(Direction.TOP, Direction.RIGHT_TOP),
            List.of(Direction.DOWN, Direction.LEFT_DOWN),
            List.of(Direction.DOWN, Direction.RIGHT_DOWN),
            List.of(Direction.LEFT, Direction.LEFT_TOP),
            List.of(Direction.LEFT, Direction.LEFT_DOWN),
            List.of(Direction.RIGHT, Direction.RIGHT_TOP),
            List.of(Direction.RIGHT, Direction.RIGHT_DOWN)
    );

    @Override
    public List<Position> getPath(Position from, Position to) {
        return HORSE_MOVE_SEQUENCES.stream()
                .map(sequence -> createPath(from, sequence))
                .flatMap(Optional::stream)
                .filter(path -> isDestination(path, to))
                .findFirst()
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean canMove(List<Place> places, Side fromSide) {
        if (places.size() != 2) {
            return false;
        }

        if (isBlockedAtFirstStep(places)) {
            return false;
        }
        return !isDestinationBlocked(places, fromSide);
    }

    private Optional<List<Position>> createPath(Position from, List<Direction> sequence) {
        Direction first = sequence.get(0);
        Direction second = sequence.get(1);

        return from.moveIfInBounds(first)
                .flatMap(firstPos ->
                        firstPos.moveIfInBounds(second)
                                .map(secondPos -> List.of(firstPos, secondPos))
                );
    }

    private boolean isDestination(List<Position> path, Position to) {
        return path.get(1).equals(to);
    }

    private boolean isBlockedAtFirstStep(List<Place> places) {
        Place first = places.get(0);
        return !first.isEmpty();
    }

    private boolean isDestinationBlocked(List<Place> places, Side fromSide) {
        Place dest = places.get(1);
        return dest.hasSide(fromSide);
    }
}
