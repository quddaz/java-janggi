package domain.place.moveStrategy;

import domain.position.Position;
import java.util.Arrays;

public enum Direction {
    TOP(1, 0),
    DOWN(-1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    LEFT_TOP(1, -1),
    RIGHT_TOP(1, 1),
    LEFT_DOWN(-1, -1),
    RIGHT_DOWN(-1, 1);

    private final int row;
    private final int column;

    Direction(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Direction straight(Position from, Position to) {
        int row = Integer.compare(to.getColumn(), from.getColumn());
        int column = Integer.compare(to.getRow(), from.getRow());

        if (!(from.getRow() == to.getRow() || from.getColumn() == to.getColumn())) {
            throw new IllegalArgumentException("직선 이동 아님");
        }

        return Arrays.stream(values())
                .filter(d -> d.row == row && d.column == column)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 방향"));
    }

    public static Direction diagonal(Position from, Position to) {
        int row = Integer.compare(to.getColumn(), from.getColumn());
        int column = Integer.compare(to.getRow(), from.getRow());

        if (Math.abs(to.getRow() - from.getRow()) !=
                Math.abs(to.getColumn() - from.getColumn())) {
            throw new IllegalArgumentException("대각선 이동 아님");
        }

        return Arrays.stream(values())
                .filter(d -> d.row == row && d.column == column)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 방향"));
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
