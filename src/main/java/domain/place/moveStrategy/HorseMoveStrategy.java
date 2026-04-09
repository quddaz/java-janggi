package domain.place.moveStrategy;

import domain.place.Place;
import domain.place.moveRule.MoveRule;
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

    private final MoveRule moveRule;

    public HorseMoveStrategy(MoveRule moveRule) {
        this.moveRule = moveRule;
    }

    @Override
    public List<Position> getPath(Position fromPosition, Position targetPosition) {
        return HORSE_MOVE_SEQUENCES.stream()
                .map(sequence -> createPath(fromPosition, sequence))
                .flatMap(Optional::stream)
                .filter(this::isValidPath)
                .filter(path -> isDestination(path, targetPosition))
                .findFirst()
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean canMove(List<Place> places, Side movingSide) {
        return moveRule.canMove(places, movingSide);
    }

    private Optional<List<Position>> createPath(Position fromPosition, List<Direction> sequence) {
        Direction firstDirection = sequence.get(0);
        Direction secondDirection = sequence.get(1);

        return fromPosition.moveIfInBounds(firstDirection)
                .flatMap(firstPosition ->
                        firstPosition.moveIfInBounds(secondDirection)
                                .map(secondPosition -> List.of(firstPosition, secondPosition))
                );
    }

    private boolean isValidPath(List<Position> path) {
        return path.size() == 2;
    }

    private boolean isDestination(List<Position> path, Position targetPosition) {
        return isValidPath(path) && path.get(1).equals(targetPosition);
    }

}
