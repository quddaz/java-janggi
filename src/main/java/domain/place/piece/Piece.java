package domain.place.piece;

import domain.place.Place;
import domain.place.moveStrategy.MoveStrategy;
import domain.position.Position;
import java.util.List;
import java.util.Optional;

public abstract class Piece implements Place {

    private final Side side;
    private final MoveStrategy moveStrategy;

    public Piece(Side side, MoveStrategy moveStrategy) {
        this.side = side;
        this.moveStrategy = moveStrategy;
    }

    public abstract PieceSymbol getSymbol();

    @Override
    public String getFormat() {
        return getSymbol().display();
    }

    @Override
    public int getScore() {
        return getSymbol().getScore();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isSameSymbol(PieceSymbol pieceSymbol) {
        return getSymbol() == pieceSymbol;
    }

    @Override
    public boolean hasSide(Side side) {
        return this.side.equals(side);
    }

    @Override
    public Optional<Side> getSide() {
        return Optional.of(side);
    }

    @Override
    public List<Position> getPath(Position from, Position to) {
        return moveStrategy.getPath(from, to);
    }

    @Override
    public boolean canMove(List<Place> path) {
        return moveStrategy.canMove(path, side);
    }

}
