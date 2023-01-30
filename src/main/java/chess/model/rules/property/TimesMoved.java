package chess.model.rules.property;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;

import java.util.List;

public class TimesMoved extends Property {

    public TimesMoved(List<Object> args) {
        super(args);
    }

    @Override
    public boolean verifyProperty(PieceType pt, Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return false;
        if (pt.relativeNeighbor != null) {
            Coordinate targetCord = new Coordinate(c.x() + pt.relativeNeighbor.xVector(), c.y() + pt.relativeNeighbor.yVector());
            p = getPiece(targetCord, b);
            if (p == null) return false;
        }
        return (p.getTimesMoved() == Integer.parseInt((String)args.get(0)));
    }
}

//            if (pt.friendly && p.getTID().playerId != target.getTID().playerId) return false;
//            if (pt.enemy && p.getTID().playerId == target.getTID().playerId) return false;
//            if (pt.type == target.getTID().id) return false;