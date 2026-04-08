package domain.moveStrategy;

import static org.assertj.core.api.Assertions.assertThat;

import domain.place.Empty;
import domain.place.Place;
import domain.place.moveStrategy.ElephantMoveStrategy;
import domain.place.moveStrategy.MoveStrategy;
import domain.place.piece.Elephant;
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

class ElephantMoveStrategyTest {

    private final MoveStrategy moveStrategy = new ElephantMoveStrategy();

    static Stream<Arguments> validMoves() {
        return Stream.of(
                Arguments.of(new Position(5, 5), new Position(7, 8)),
                Arguments.of(new Position(5, 5), new Position(3, 8)),
                Arguments.of(new Position(5, 5), new Position(7, 2)),
                Arguments.of(new Position(5, 5), new Position(3, 2)),
                Arguments.of(new Position(5, 5), new Position(8, 7)),
                Arguments.of(new Position(5, 5), new Position(8, 3)),
                Arguments.of(new Position(5, 5), new Position(2, 7)),
                Arguments.of(new Position(5, 5), new Position(2, 3))
        );
    }

    @ParameterizedTest
    @DisplayName("상은 다양한 방향으로 정상 이동할 수 있다")
    @MethodSource("validMoves")
    void can_move_in_all_valid_directions(Position from, Position to) {
        // given
        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("상은 상대 기물을 잡을 수 있다")
    void capture_opponent() {
        // given
        Position from = new Position(5, 5);
        Position to = new Position(7, 8);

        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(2, new Elephant(Side.HAN, moveStrategy));

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("상은 첫 번째 경로에 장애물이 있으면 이동 불가")
    void blocked_first_path() {
        // given
        Position from = new Position(5, 5);
        Position to = new Position(7, 8);

        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(0, new Elephant(Side.HAN, moveStrategy));

        // when
        boolean result = moveStrategy.canMove(places, Side.CHO);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("상은 두 번째 경로에 장애물이 있으면 이동 불가")
    void blocked_second_path() {
        // given
        Position from = new Position(5, 5);
        Position to = new Position(7, 8);

        List<Position> path = moveStrategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(1, new Elephant(Side.HAN, moveStrategy));

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
