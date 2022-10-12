package chess.model.piece;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.rules.action.Action;
import chess.model.rules.Condition;
import chess.model.rules.MovementPattern;

public class Piece {

    //Type Behavior
    private PieceBehavior behavior;

    //Instance Variables
    private Map<Integer, Flag> flags;
    private PieceStatistics statistics;

    public Piece(PieceBehavior behavior, Coordinate coordinate) {
        this.behavior = behavior;
        flags = new HashMap<>();
        statistics = new PieceStatistics(coordinate);
    }

    public Map<Coordinate, List<Action>> getFeasibleMoves(Board board) {
        Map<Coordinate, List<Action>> feasableMoves = new HashMap<>();
        Coordinate coordinate = statistics.getCoordinate();
        for (MovementPattern mp : behavior.getMovementPattern()) {
            if (!verifyConditions(behavior.getFulfillCond().get(mp), behavior.getInhibitoryCond().get(mp), coordinate, board)) continue;
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
                feasableMoves.put(c, behavior.getActions().get(mp));
                if (piece != null && piece.behavior.getTID().playerId != behavior.getTID().playerId) break; //No movement through
                step++;
            }
        }
        return feasableMoves;
    }

    public PieceTypeID getTID() {
        return behavior.getTID();
    }

    public void updateStatistics(Coordinate c, int turn, Optional<Piece> p) {
        statistics.updateStatistics(c, turn, p);
    }

    public Coordinate getCoordinate() {
        return statistics.getCoordinate();
    }

    public int getTimesMoved() {
        return statistics.getTimesMoved();
    }

    public int getLastTurnMoved() {
        return statistics.getLastTurnNumberMoved();
    }

    public Map<Integer, Flag> getFlags() {
        return flags;
    }

    private boolean verifyConditions(List<Condition> fulfillCond, List<Condition> inhibitoryCond, Coordinate c, Board b) {
        boolean fCond = true;
        boolean iCond = false;
        if (fulfillCond != null) {
            for (Condition cond : fulfillCond) {
                fCond &= cond.verifyCondition(getTID(), c, b);
            }
        }
        if (inhibitoryCond != null) {
            for (Condition cond : inhibitoryCond) {
                iCond |= cond.verifyCondition(getTID(), c, b);
            }
        }
        return (fCond && !iCond);
    }

}
