package model.piece;

import java.util.List;
import java.util.Map;

import model.rules.Condition;
import model.rules.MovementPattern;

public class PieceBehavior {

    private PieceTypeID TID;
    private List<MovementPattern> movementPattern;
    private Map<MovementPattern, Condition> fulfillCond;
    private Map<MovementPattern, Condition> inhibitoryCond;

    public PieceBehavior(PieceTypeID TID) {
        this.TID = TID;
    }

    public void setBehavior(List<MovementPattern> movementPattern, Map<MovementPattern, Condition> fulfillCond, Map<MovementPattern, Condition> inhibitoryCond) {
        this.movementPattern = movementPattern;
        this.fulfillCond = fulfillCond;
        this.inhibitoryCond = inhibitoryCond;
    }

    public PieceTypeID getTID() {
        return TID;
    }

    public List<MovementPattern> getMovementPattern() {
        return movementPattern;
    }

    public Map<MovementPattern, Condition> getFulfillCond() {
        return fulfillCond;
    }

    public Map<MovementPattern, Condition> getInhibitoryCond() {
        return inhibitoryCond;
    }
    
}
