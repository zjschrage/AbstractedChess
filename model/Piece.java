package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Piece {

    //Type Specific
    private Player playerId;
    private List<MovementPattern> movementPattern;
    private Map<MovementPattern, Condition> fulfillCond;
    private Map<MovementPattern, Condition> inhibitoryCond;
    //Instance Specific
    private Coordinate coordinate;
    private int timesMoved;
    private int lastTurnNumberMoved;

    public Piece() {

    }

    public void init(Player playerId, List<MovementPattern> movementPattern, Map<MovementPattern, Condition> fulfillCond,  Map<MovementPattern, Condition> inhibitoryCond, Coordinate coordinate) {
        this.playerId = playerId;
        this.movementPattern = movementPattern;
        this.fulfillCond = fulfillCond;
        this.inhibitoryCond = inhibitoryCond;
        this.coordinate = coordinate;
    }

    public List<Coordinate> getFeasableMoves(Board board) {
        List<Coordinate> feasableMoves = new ArrayList<>();
        for (MovementPattern mp : movementPattern) {
            if (verifyConditions(fulfillCond.get(mp), inhibitoryCond.get(mp), coordinate, board)) continue;
            int xVec = mp.xVector();
            int yVec = mp.yVector();
            int rep = mp.repetitions();
            if (rep == -1) rep = Integer.MAX_VALUE;
            int step = 1;
            while (true) {
                Coordinate c = new Coordinate(coordinate.x() + step*xVec, coordinate.y() + step*yVec);
                if (!board.checkInBounds(c) || step > rep) break;
                Piece piece = board.getBoard().get(c).getPiece();
                if (piece != null && piece.playerId == playerId) break;
                feasableMoves.add(c);
                if (piece != null && piece.playerId != playerId) break;
                step++;
            }
        }
        return feasableMoves;
    }

    private boolean verifyConditions(Condition fulfillCond, Condition inhibitoryCond, Coordinate c, Board b) {
        boolean fCond = true;
        boolean iCond = false;
        if (fulfillCond != null) fCond = fulfillCond.verifyCondition(c, b, ConditionType.FULFILL);
        if (inhibitoryCond != null) iCond = inhibitoryCond.verifyCondition(c, b, ConditionType.INHIBIT);
        return !(fCond && !iCond);
    }

    public String toString() {
        return String.format("%d", playerId.ordinal());
    }

}
