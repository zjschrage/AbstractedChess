package chess.model.rules.property;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Flag;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.model.play.PlayManager;

import java.util.List;

public class CheckFlag extends Property {

    public CheckFlag(PropertyType propertyType, List<Object> args) {
        super(propertyType, args);
    }

    @Override
    public boolean verifyProperty(PieceType pt, Coordinate c, Board b) {
        if (pt.relativeNeighbor == null) return false;
        c = new Coordinate(c.x() + pt.relativeNeighbor.xVector(), c.y() + pt.relativeNeighbor.yVector());
        Piece p = getPiece(c, b);
        if (p == null) return false;
        Flag f = p.getFlags().get(Integer.parseInt((String)args.get(0)));
        if (f == null) return false;
        if (!f.isActive(PlayManager.turn)) return false;
        return (f.getValue() == Integer.parseInt((String)args.get(1)));
    }

}
