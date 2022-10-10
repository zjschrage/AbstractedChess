package chess.model.rules;

import java.util.List;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Flag;
import chess.model.piece.Piece;
import chess.model.play.PlayManager;

public class Action {
    
    private ActionType actionType;
    private List<Object> args;
    
    public Action(ActionType actionType, List<Object> args) {
        this.actionType = actionType;
        this.args = args;
    }

    public void execute(Coordinate c, Board b) {
        switch (actionType) {
            case SET_FLAG -> setFlag(c, b);
            case MOVE -> move(c, b);
            case REMOVE -> remove(c, b);
            default -> System.out.println("Default Action");
        };
    }

    private void setFlag(Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return;
        p.getFlags().put(Integer.parseInt((String)args.get(0)), new Flag(Integer.parseInt((String)args.get(1)), PlayManager.turn, Integer.parseInt((String)args.get(2))));
    }

    private void move(Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return;
        MovementPattern from = MovementPattern.class.cast(args.get(0));
        MovementPattern to = MovementPattern.class.cast(args.get(1));
        Coordinate fromCord = new Coordinate(c.x() + from.xVector(), c.y() + from.yVector());
        Coordinate toCord = new Coordinate(c.x() + to.xVector(), c.y() + to.yVector());
        Piece movPiece = getPiece(fromCord, b);
        if (movPiece == null || !b.checkInBounds(toCord)) return;
        movPiece.updateStatistics(toCord, PlayManager.turn);
        b.getBoard().get(toCord).placePiece(b.getBoard().get(fromCord).pickUpPiece());
    }

    private void remove(Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return;
        MovementPattern vec = MovementPattern.class.cast(args.get(0));
        Coordinate rem = new Coordinate(c.x() + vec.xVector(), c.y() + vec.yVector());
        Piece remPiece = getPiece(rem, b);
        if (remPiece == null) return;
        b.getBoard().get(rem).pickUpPiece();
    }

    private Piece getPiece(Coordinate c, Board b) {
        if (!b.checkInBounds(c)) return null;
        else return b.getBoard().get(c).getPiece();
    }

    public String toString() {
        return String.format("[type=%s, args=%s]", actionType.toString(), args.toString());
    }
}
