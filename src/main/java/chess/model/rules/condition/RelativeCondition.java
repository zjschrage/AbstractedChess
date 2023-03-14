package chess.model.rules.condition;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.board.Tile;
import chess.model.piece.PieceType;
import chess.model.piece.PieceTypeID;
import chess.model.rules.MovementPattern;

public class RelativeCondition extends Condition<MovementPattern> {
    public RelativeCondition() {
        super();
    }

    @Override
    public boolean verifyCondition(PieceTypeID ptid, Coordinate c, Board b) {
        for (PieceType p : condition.keySet()) {
            MovementPattern relVec = condition.get(p);
            Coordinate relPos = new Coordinate(c.x() + relVec.xVector(), c.y() + relVec.yVector());
            Tile tile = b.getBoard().get(relPos);
            if (pieceTypeCases(tile, p, ptid).isPresent()) return pieceTypeCases(tile, p, ptid).get();
        }
        return false;
    }
}
