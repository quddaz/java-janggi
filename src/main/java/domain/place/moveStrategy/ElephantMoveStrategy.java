package domain.place.moveStrategy;

import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ElephantMoveStrategy implements MoveStrategy {

    public static final List<List<Direction>> ELEPHANT_MOVE_SEQUENCES = List.of(
            List.of(Direction.TOP, Direction.LEFT_TOP, Direction.LEFT_TOP),
            List.of(Direction.TOP, Direction.RIGHT_TOP, Direction.RIGHT_TOP),
            List.of(Direction.DOWN, Direction.LEFT_DOWN, Direction.LEFT_DOWN),
            List.of(Direction.DOWN, Direction.RIGHT_DOWN, Direction.RIGHT_DOWN),
            List.of(Direction.LEFT, Direction.LEFT_TOP, Direction.LEFT_TOP),
            List.of(Direction.LEFT, Direction.LEFT_DOWN, Direction.LEFT_DOWN),
            List.of(Direction.RIGHT, Direction.RIGHT_TOP, Direction.RIGHT_TOP),
            List.of(Direction.RIGHT, Direction.RIGHT_DOWN, Direction.RIGHT_DOWN)
    );

    @Override
    public List<Position> getPath(Position from, Position to) {
        return ELEPHANT_MOVE_SEQUENCES.stream()
                .map(sequence -> moveSteps(from, sequence))
                .flatMap(Optional::stream)
                .filter(this::isValidPath)
                .filter(path -> isDestination(path, to))
                .findFirst()
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean canMove(List<Place> places, Side fromSide) {
        if (!isValidPlaceSize(places)) {
            return false;
        }

        return !isBlockedAtFirstStep(places)
                && !isBlockedAtSecondStep(places)
                && !isDestinationBlocked(places, fromSide);
    }

    private Optional<List<Position>> moveSteps(Position from, List<Direction> sequence) {
        List<Position> result = new ArrayList<>();
        Position current = from;

        for (Direction direction : sequence) {
            Optional<Position> next = moveStep(current, direction, result);
            if (next.isEmpty()) return Optional.empty();
            current = next.get();
        }

        return Optional.of(result);
    }

    private Optional<Position> moveStep(Position current, Direction direction, List<Position> result) {
        Optional<Position> next = current.moveIfInBounds(direction);
        if (next.isEmpty()) return Optional.empty();

        Position moved = next.get();
        result.add(moved);
        return Optional.of(moved);
    }

    private boolean isValidPath(List<Position> path) {
        return path.size() == 3;
    }

    private boolean isDestination(List<Position> path, Position to) {
        return isValidPath(path) && path.get(2).equals(to);
    }

    private boolean isValidPlaceSize(List<Place> places) {
        return places.size() == 3;
    }

    private boolean isBlockedAtFirstStep(List<Place> places) {
        return !places.getFirst().isEmpty();
    }

    private boolean isBlockedAtSecondStep(List<Place> places) {
        return !places.get(1).isEmpty();
    }

    private boolean isDestinationBlocked(List<Place> places, Side fromSide) {
        return places.get(2).hasSide(fromSide);
    }
}
