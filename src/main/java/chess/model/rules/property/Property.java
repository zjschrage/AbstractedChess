package chess.model.rules.property;

import java.util.List;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;

public abstract class Property {

    protected List<Object> args;
    
    public Property(List<Object> args) {
        this.args = args;
    }

    public abstract boolean verifyProperty(PieceType pt, Coordinate c, Board b);

    protected Piece getPiece(Coordinate c, Board b) {
        if (!b.checkInBounds(c)) return null;
        else return b.getBoard().get(c).getPiece();
    }

    public String toString() {
        return String.format("[args=%s]", args.toString());
    }

}
