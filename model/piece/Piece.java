package model.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.board.Board;
import model.board.Coordinate;
import model.rules.Condition;
import model.rules.ConditionType;
import model.rules.MovementPattern;

public class Piece {

    //Type Behavior
    private PieceBehavior behavior;

    //Instance Variables
    private Coordinate coordinate;
    private int timesMoved;
    private int lastTurnNumberMoved;

    public Piece() {
        timesMoved = 0;
        lastTurnNumberMoved = -1;
    }

    public Piece(PieceBehavior behavior) {
        this.behavior = behavior;
        timesMoved = 0;
        lastTurnNumberMoved = -1;
    }

    public Piece(PieceBehavior behavior, Coordinate coordinate) {
        this.behavior = behavior;
        this.coordinate = coordinate;
        timesMoved = 0;
        lastTurnNumberMoved = -1;
    }

    // public void init(Player playerId, Set<MovementPattern> movementPattern, Map<MovementPattern, Condition> fulfillCond,  Map<MovementPattern, Condition> inhibitoryCond, Coordinate coordinate) {
    //     this.behavior.getTID().playerId = playerId;
    //     this.behavior.setBehavior(movementPattern, fulfillCond, inhibitoryCond);
    //     this.coordinate = coordinate;
    // }

    public List<Coordinate> getFeasableMoves(Board board) {
        List<Coordinate> feasableMoves = new ArrayList<>();
        for (MovementPattern mp : behavior.getMovementPattern()) {
            if (verifyConditions(behavior.getFulfillCond().get(mp), behavior.getInhibitoryCond().get(mp), coordinate, board)) continue;
            int xVec = mp.xVector();
            int yVec = mp.yVector();
            int rep = mp.repetitions();
            if (rep == -1) rep = Integer.MAX_VALUE;
            int step = 1;
            while (true) {
                Coordinate c = new Coordinate(coordinate.x() + step*xVec, coordinate.y() + step*yVec);
                if (!board.checkInBounds(c) || step > rep) break;
                Piece piece = board.getBoard().get(c).getPiece();
                if (piece != null && piece.behavior.getTID().playerId == behavior.getTID().playerId) break; //No self capture
                feasableMoves.add(c);
                if (piece != null && piece.behavior.getTID().playerId != behavior.getTID().playerId) break; //No movement through
                step++;
            }
        }
        return feasableMoves;
    }

    public PieceTypeID getTID() {
        return behavior.getTID();
    }

    public void updateStatistics(Coordinate c, int turn) {
        coordinate = c;
        timesMoved++;
        lastTurnNumberMoved = turn;
    }

    public int getTimesMoved() {
        return timesMoved;
    }

    public int getLastTurnMoved() {
        return lastTurnNumberMoved;
    }

    private boolean verifyConditions(List<Condition> fulfillCond, List<Condition> inhibitoryCond, Coordinate c, Board b) {
        boolean fCond = true;
        boolean iCond = false;
        if (fulfillCond != null) {
            for (Condition cond : fulfillCond) {
                fCond &= cond.verifyCondition(getTID(), c, b, ConditionType.FULFILL);
            }
        }
        if (inhibitoryCond != null) {
            for (Condition cond : inhibitoryCond) {
                iCond |= cond.verifyCondition(getTID(), c, b, ConditionType.INHIBIT);
            }
        }
        return !(fCond && !iCond);
    }

}
