package domain.moveStrategy;

import static org.assertj.core.api.Assertions.assertThat;

import domain.place.Empty;
import domain.place.Place;
import domain.place.moveStrategy.JumpMoveStrategy;
import domain.place.moveStrategy.MoveStrategy;
import domain.place.piece.Cannon;
import domain.place.piece.Side;
import domain.place.piece.Soldier;
import domain.position.Position;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JumpMoveStrategyTest {

    private final MoveStrategy moveStrategy = new JumpMoveStrategy();

    static Stream<Arguments> validJumpMoves() {
        return Stream.of(
                // 일반 직선 점프
                Arguments.of(new Position(1, 1), new Position(1, 7)),
                Arguments.of(new Position(1, 7), new Position(1, 1)),
                Arguments.of(new Position(1, 1), new Position(7, 1)),
                Arguments.of(new Position(7, 1), new Position(1, 1)),

                // 궁성 내부 점프 (가로/세로)
                Arguments.of(new Position(1, 4), new Position(3, 6)),
                Arguments.of(new Position(3, 4), new Position(1, 6)),
                Arguments.of(new Position(10, 4), new Position(8, 6)),
                Arguments.of(new Position(10, 6), new Position(8, 4))
        );
    }

    @ParameterizedTest
    @DisplayName("포는 기물을 하나 넘어서 이동할 수 있다")
    @MethodSource("validJumpMoves")
    void can_move_over_one_piece(Position from, Position to) {
        // given
        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(places.size() / 2, new Soldier(Side.CHO, null));

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("포는 기물을 넘어 상대 기물을 먹을 수 있다")
    void move_over_piece_and_capture() {
        // given
        Position from = new Position(1, 1);
        Position to = new Position(1, 9);

        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(1, new Soldier(Side.CHO, null));
        places.set(places.size() - 1, new Soldier(Side.HAN, null));

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("포는 포를 넘을 수 없다")
    void cannot_jump_over_cannon() {
        // given
        Position from = new Position(1, 1);
        Position to = new Position(1, 7);

        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(1, new Cannon(Side.CHO, moveStrategy));

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("포는 넘을 기물이 없으면 이동할 수 없다")
    void cannot_move_without_screen() {
        // given
        Position from = new Position(1, 1);
        Position to = new Position(1, 7);

        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("포는 장애물이 여러 개면 이동할 수 없다")
    void cannot_move_with_multiple_obstacles() {
        // given
        Position from = new Position(1, 1);
        Position to = new Position(1, 7);

        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(1, new Soldier(Side.CHO, null));
        places.set(2, new Soldier(Side.CHO, null));

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isFalse();
    }

    private List<Place> emptyPlaces(List<Position> path) {
        return path.stream()
                .map(p -> new Empty())
                .collect(Collectors.toList());
    }
}
