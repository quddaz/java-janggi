package domain.moveStrategy;

import static org.assertj.core.api.Assertions.assertThat;

import domain.place.Empty;
import domain.place.Place;
import domain.place.moveRule.OneStepMoveRule;
import domain.place.moveStrategy.MoveStrategy;
import domain.place.moveStrategy.OneStepMoveStrategy;
import domain.place.piece.Guard;
import domain.place.piece.Side;
import domain.position.Position;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OneStepMoveStrategyTest {

    private final MoveStrategy strategy = new OneStepMoveStrategy(new OneStepMoveRule());

    static Stream<Arguments> validMoves() {
        return Stream.of(
                Arguments.of(new Position(2, 5), new Position(3, 5)),
                Arguments.of(new Position(3, 5), new Position(2, 5)),
                Arguments.of(new Position(2, 5), new Position(2, 4)),
                Arguments.of(new Position(2, 5), new Position(2, 6)),
                Arguments.of(new Position(2, 5), new Position(1, 4)),
                Arguments.of(new Position(2, 5), new Position(1, 6)),
                Arguments.of(new Position(2, 5), new Position(3, 4)),
                Arguments.of(new Position(2, 5), new Position(3, 6))
        );
    }

    @ParameterizedTest
    @DisplayName("한 칸 이동 가능 (궁성 + 대각선)")
    @MethodSource("validMoves")
    void can_move_one_step(Position from, Position to) {
        // given
        List<Position> path = strategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);

        // when
        boolean result = strategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("두 칸 이동 불가")
    void cannot_move_more_than_one_step() {
        // given
        Position from = new Position(2, 5);
        Position to = new Position(10, 5);

        List<Position> path = strategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);

        // when
        boolean result = strategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("아군 위치로 이동 불가")
    void cannot_move_to_same_team() {
        // given
        Position from = new Position(2, 5);
        Position to = new Position(3, 5);

        List<Position> path = strategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);

        places.set(places.size() - 1, new Guard(Side.CHO, strategy));

        // when
        boolean result = strategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isFalse();
    }

    private List<Place> emptyPlaces(List<Position> path) {
        return path.stream()
                .map(p -> new Empty())
                .collect(Collectors.toList());
    }
}
