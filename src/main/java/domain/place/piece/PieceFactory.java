package domain.place.piece;

import domain.place.Place;
import domain.place.moveRule.CannonMoveRule;
import domain.place.moveRule.ChariotMoveRule;
import domain.place.moveRule.ElephantMoveRule;
import domain.place.moveRule.HorseRule;
import domain.place.moveRule.OneStepMoveRule;
import domain.place.moveStrategy.ElephantMoveStrategy;
import domain.place.moveStrategy.HorseMoveStrategy;
import domain.place.moveStrategy.CannonMoveStrategy;
import domain.place.moveStrategy.OneStepMoveStrategy;
import domain.place.moveStrategy.SoldierMoveStrategy;
import domain.place.moveStrategy.ChariotMoveStrategy;
import java.util.Arrays;
import java.util.function.Function;

public enum PieceFactory {

    GENERAL("궁", side -> new General(side, new OneStepMoveStrategy(new OneStepMoveRule()))),
    GUARD("사", side -> new Guard(side, new OneStepMoveStrategy(new OneStepMoveRule()))),
    HORSE("마", side -> new Horse(side, new HorseMoveStrategy(new HorseRule()))),
    ELEPHANT("상", side -> new Elephant(side, new ElephantMoveStrategy(new ElephantMoveRule()))),
    CHARIOT("차", side -> new Chariot(side, new ChariotMoveStrategy(new ChariotMoveRule()))),
    CANNON("포", side -> new Cannon(side, new CannonMoveStrategy(new CannonMoveRule()))),
    SOLDIER("졸", side -> new Soldier(side, new SoldierMoveStrategy(side, new OneStepMoveRule())));

    private final String code;
    private final Function<Side, Place> factory;

    PieceFactory(String code, Function<Side, Place> factory) {
        this.code = code;
        this.factory = factory;
    }

    public static PieceFactory from(String target) {
        return Arrays.stream(PieceFactory.values())
                .filter(pieceFactory -> pieceFactory.code.equals(target))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 기물 정보를 찾을 수 없습니다"));
    }

    public Place createPlace(Side side) {
        return factory.apply(side);
    }
}