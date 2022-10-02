package model.piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.rules.Condition;
import model.rules.MovementPattern;

public class PieceBehavior {

    private PieceTypeID TID;
    private Set<MovementPattern> movementPattern;
    private Map<MovementPattern, List<Condition>> fulfillCond;
    private Map<MovementPattern, List<Condition>> inhibitoryCond;

    public PieceBehavior(PieceTypeID TID) {
        this.TID = TID;
        this.movementPattern = new HashSet<>();
        this.fulfillCond = new HashMap<>();
        this.inhibitoryCond = new HashMap<>();
    }

    public void setBehavior(Set<MovementPattern> movementPattern, Map<MovementPattern, List<Condition>> fulfillCond, Map<MovementPattern, List<Condition>> inhibitoryCond) {
        this.movementPattern = movementPattern;
        this.fulfillCond = fulfillCond;
        this.inhibitoryCond = inhibitoryCond;
    }

    public void addMovementPattern(MovementPattern mp) {
        movementPattern.add(mp);
    }

    public void addFulfillCond(MovementPattern mp, Condition c) {
        List<Condition> cs = fulfillCond.get(mp);
        if (cs == null) fulfillCond.put(mp, new ArrayList<>());
        fulfillCond.get(mp).add(c);
    }

    public void addInhibitoryCond(MovementPattern mp, Condition c) {
        List<Condition> cs = inhibitoryCond.get(mp);
        if (cs == null) inhibitoryCond.put(mp, new ArrayList<>());
        inhibitoryCond.get(mp).add(c);
    }

    public PieceTypeID getTID() {
        return TID;
    }

    public Set<MovementPattern> getMovementPattern() {
        return movementPattern;
    }

    public Map<MovementPattern, List<Condition>> getFulfillCond() {
        return fulfillCond;
    }

    public Map<MovementPattern, List<Condition>> getInhibitoryCond() {
        return inhibitoryCond;
    }
    
}
