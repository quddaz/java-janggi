package domain.place.moveStrategy;

import domain.place.PalaceArea;
import domain.place.Place;
import domain.place.moveRule.MoveRule;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CannonMoveStrategy implements MoveStrategy {

    private final MoveRule moveRule;

    public CannonMoveStrategy(MoveRule moveRule) {
        this.moveRule = moveRule;
    }

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
        return moveRule.canMove(places, movingSide);
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

}
