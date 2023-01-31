package chess.model.rules.property;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;

import java.sql.SQLOutput;
import java.util.List;

public class CheckCord extends Property {

    public CheckCord(List<Object> args) {
        super(args);
    }
    @Override
    public boolean verifyProperty(PieceType pt, Coordinate c, Board b) {
        int x = -1;
        int y = -1;
        String xStr = (String)args.get(0);
        String yStr = (String)args.get(1);
        if (!xStr.equals("*")) x = Integer.parseInt(xStr);
        if (!yStr.equals("*")) y = Integer.parseInt(yStr);

        if (pt.relativeNeighbor != null) c = new Coordinate(c.x() + pt.relativeNeighbor.xVector(), c.y() + pt.relativeNeighbor.yVector());
        Piece p = getPiece(c, b);
        if (p == null) return false;

        return (x == -1 || x == c.x()) && (y == -1 || y == c.y());
    }

}
