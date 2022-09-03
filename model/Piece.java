package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Piece {

    //Type Specific
    private int typeId;
    private List<MovementPattern> movementPattern;
    private Map<MovementPattern, Condition> conditions;
    private Map<MovementPattern, Action> actions;
    //Instance Specific
    private Coordinate coordinate;
    private int playerId;
    private int timesMoved;
    private int lastTurnNumberMoved;

    public Piece(int typeId, List<MovementPattern> movementPattern, Map<MovementPattern, Condition> conditions, Map<MovementPattern, Action> actions, Coordinate coordinate) {
        this.typeId = typeId;
        this.movementPattern = movementPattern;
        this.conditions = conditions;
        this.actions = actions;
        this.coordinate = coordinate;
    }

    public List<Coordinate> getFeasableMoves(Board board) {
        List<Coordinate> feasableMoves = new ArrayList<>();
        for (MovementPattern mp : movementPattern) {
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
                step++;
            }
        }
        return feasableMoves;
    }

    public String toString() {
        return String.format("%d", typeId);
    }

}
