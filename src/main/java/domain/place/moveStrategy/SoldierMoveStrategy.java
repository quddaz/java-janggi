package domain.place.moveStrategy;

import domain.place.PalaceArea;
import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.Collections;
import java.util.List;

public class SoldierMoveStrategy implements MoveStrategy {

    private final List<Direction> normalDirections;
    private final List<Direction> palaceDirections;

    public SoldierMoveStrategy(Side side) {
        this.normalDirections = initNormalDirections(side);
        this.palaceDirections = initPalaceDirections(side);
    }

    private List<Direction> initNormalDirections(Side side) {
        if (side == Side.CHO) {
            return List.of(Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        }
        return List.of(Direction.TOP, Direction.LEFT, Direction.RIGHT);
    }

    private List<Direction> initPalaceDirections(Side side) {
        if (side == Side.CHO) {
            return List.of(Direction.LEFT_DOWN, Direction.RIGHT_DOWN);
        }
        return List.of(Direction.LEFT_TOP, Direction.RIGHT_TOP);
    }

    @Override
    public List<Position> getPath(Position from, Position target) {

        if (isInsidePalace(from, target)) {
            return getPalacePath(from, target);
        }

        if (!from.isStraightWith(target)) {
            return Collections.emptyList();
        }

        return getNormalPath(from, target);
    }

    private boolean isInsidePalace(Position from, Position to) {
        return PalaceArea.isInsideSpecialPalace(from) && PalaceArea.isInsideSpecialPalace(to);
    }

    private List<Position> getPalacePath(Position from, Position target) {
        return palaceDirections.stream()
                .flatMap(d -> from.moveIfInBounds(d).stream())
                .filter(target::equals)
                .map(List::of)
                .findFirst()
                .orElse(Collections.emptyList());
    }

    private List<Position> getNormalPath(Position from, Position target) {
        return normalDirections.stream()
                .flatMap(d -> from.moveIfInBounds(d).stream())
                .filter(target::equals)
                .map(List::of)
                .findFirst()
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean canMove(List<Place> places, Side movingSide) {
        return isValidPlaceSize(places) && !isDestinationBlocked(places, movingSide);
    }

    private boolean isValidPlaceSize(List<Place> places) {
        return !places.isEmpty();
    }

    private boolean isDestinationBlocked(List<Place> places, Side movingSide) {
        return getLast(places).hasSide(movingSide);
    }

    private Place getLast(List<Place> places) {
        return places.getLast();
    }
}
