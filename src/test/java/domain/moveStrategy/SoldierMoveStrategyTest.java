package domain.moveStrategy;

import static org.assertj.core.api.Assertions.assertThat;

import domain.place.Empty;
import domain.place.Place;
import domain.place.moveRule.OneStepMoveRule;
import domain.place.moveStrategy.MoveStrategy;
import domain.place.moveStrategy.SoldierMoveStrategy;
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

class SoldierMoveStrategyTest {

    static Stream<Arguments> validMoves() {
        return Stream.of(
                // CHO (위로 전진)
                Arguments.of(Side.CHO, new Position(2, 1), new Position(1, 1)),
                Arguments.of(Side.CHO, new Position(2, 2), new Position(1, 2)),
                Arguments.of(Side.CHO, new Position(2, 2), new Position(2, 1)),
                Arguments.of(Side.CHO, new Position(2, 2), new Position(2, 3)),

                // HAN (아래로 전진)
                Arguments.of(Side.HAN, new Position(9, 2), new Position(10, 2)),
                Arguments.of(Side.HAN, new Position(9, 5), new Position(10, 5)),
                Arguments.of(Side.HAN, new Position(9, 5), new Position(9, 4)),
                Arguments.of(Side.HAN, new Position(9, 5), new Position(9, 6))
        );
    }

    @ParameterizedTest
    @DisplayName("졸 이동 가능 (궁성 포함)")
    @MethodSource("validMoves")
    void can_move(Side side, Position from, Position to) {
        // given
        MoveStrategy strategy = new SoldierMoveStrategy(side, new OneStepMoveRule());
        List<Position> path = strategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);

        // when
        boolean result = strategy.canMove(places, side);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("아군 위치 이동 불가")
    void cannot_move_to_same_team() {
        // given
        Side side = Side.CHO;
        MoveStrategy strategy = new SoldierMoveStrategy(side, new OneStepMoveRule());

        Position from = new Position(2, 2);
        Position to = new Position(2, 3);

        List<Position> path = strategy.getPath(from, to);
        List<Place> places = emptyPlaces(path);
        places.set(places.size() - 1, new Soldier(side, strategy));

        // when
        boolean result = strategy.canMove(places, side);

        // then
        assertThat(result).isFalse();
    }

    private List<Place> emptyPlaces(List<Position> path) {
        return path.stream()
                .map(p -> new Empty())
                .collect(Collectors.toList());
    }
}

