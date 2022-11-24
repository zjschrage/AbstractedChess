package chess.model.piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chess.model.rules.action.Action;
import chess.model.rules.Condition;
import chess.model.rules.MovementPattern;

public class PieceBehavior {

    private PieceTypeID TID;
    private Set<MovementPattern> movementPattern;
    private Map<MovementPattern, List<Condition>> fulfillCond;
    private Map<MovementPattern, List<Condition>> inhibitoryCond;
    private Map<MovementPattern, List<Action>> actions;
    private Map<Action, List<Condition>> captureActions;

    public PieceBehavior(PieceTypeID TID) {
        this.TID = TID;
        this.movementPattern = new HashSet<>();
        this.fulfillCond = new HashMap<>();
        this.inhibitoryCond = new HashMap<>();
        this.actions = new HashMap<>();
        this.captureActions = new HashMap<>();
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

    public void addAction(MovementPattern mp, Action a) {
        List<Action> as = actions.get(mp);
        if (as == null) actions.put(mp, new ArrayList<>());
        actions.get(mp).add(a);
    }

    public void addCaptureAction(Action a, Condition c) {
        List<Condition> cs = captureActions.get(a);
        if (cs == null) captureActions.put(a, new ArrayList<>());
        if (c != null) captureActions.get(a).add(c);
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

    public Map<MovementPattern, List<Action>> getActions() {
        return actions;
    }

    public Map<Action, List<Condition>> getCaptureActions() { return captureActions; }
    
}
