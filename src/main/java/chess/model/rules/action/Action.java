package chess.model.rules.action;

import java.util.List;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;

public abstract class Action {

    protected List<Object> args;
    
    public Action(List<Object> args) {
        this.args = args;
    }

    public abstract void execute(Coordinate c, Board b);

    protected Piece getPiece(Coordinate c, Board b) {
        if (!b.checkInBounds(c)) return null;
        else return b.getBoard().get(c).getPiece();
    }

    public String toString() {
        return String.format("[type=%s, args=%s]", "x", args.toString());
    }
}
