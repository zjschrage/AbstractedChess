package model.piece;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.rules.Condition;
import model.rules.MovementPattern;

public class PieceBehavior {

    private PieceTypeID TID;
    private Set<MovementPattern> movementPattern;
    private Map<MovementPattern, Condition> fulfillCond;
    private Map<MovementPattern, Condition> inhibitoryCond;

    public PieceBehavior(PieceTypeID TID) {
        this.TID = TID;
        this.movementPattern = new HashSet<>();
        this.fulfillCond = new HashMap<>();
        this.inhibitoryCond = new HashMap<>();
    }

    public void setBehavior(Set<MovementPattern> movementPattern, Map<MovementPattern, Condition> fulfillCond, Map<MovementPattern, Condition> inhibitoryCond) {
        this.movementPattern = movementPattern;
        this.fulfillCond = fulfillCond;
        this.inhibitoryCond = inhibitoryCond;
    }

    public void addMovementPattern(MovementPattern mp) {
        movementPattern.add(mp);
    }

    public void addFulfillCond(MovementPattern mp, Condition c) {
        fulfillCond.put(mp, c);
    }

    public void addInhibitoryCond(MovementPattern mp, Condition c) {
        inhibitoryCond.put(mp, c);
    }

    public PieceTypeID getTID() {
        return TID;
    }

    public Set<MovementPattern> getMovementPattern() {
        return movementPattern;
    }

    public Map<MovementPattern, Condition> getFulfillCond() {
        return fulfillCond;
    }

    public Map<MovementPattern, Condition> getInhibitoryCond() {
        return inhibitoryCond;
    }
    
}
