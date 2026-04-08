package domain.moveStrategy;

import static org.assertj.core.api.Assertions.assertThat;

import domain.place.Empty;
import domain.place.Place;
import domain.place.moveStrategy.MoveStrategy;
import domain.place.moveStrategy.StraightMoveStrategy;
import domain.place.piece.Chariot;
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

class StraightMoveStrategyTest {

    private final MoveStrategy strategy = new StraightMoveStrategy();

    static Stream<Arguments> validMoves() {
        return Stream.of(
                // 일반 직선
                Arguments.of(new Position(3, 5), new Position(3, 7)),
                Arguments.of(new Position(3, 5), new Position(3, 3)),
                Arguments.of(new Position(3, 5), new Position(5, 5)),
                Arguments.of(new Position(3, 5), new Position(1, 5)),

                // 궁성 내부 직선
                Arguments.of(new Position(2, 5), new Position(3, 5)),
                Arguments.of(new Position(2, 5), new Position(1, 5)),
                Arguments.of(new Position(2, 5), new Position(2, 4)),
                Arguments.of(new Position(2, 5), new Position(2, 6))
        );
    }

    @ParameterizedTest
    @DisplayName("직선 이동 가능")
    @MethodSource("validMoves")
    void can_move_straight(Position from, Position to) {
        // given
        List<Position> path = strategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);

        // when
        boolean result = strategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("경로 장애물 있으면 이동 불가")
    void cannot_move_when_blocked() {
        // given
        Position from = new Position(3, 5);
        Position to = new Position(3, 7);

        List<Position> path = strategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(0, new Chariot(Side.HAN, strategy));

        // when
        boolean result = strategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("도착 위치에 아군이 있으면 이동 불가")
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
