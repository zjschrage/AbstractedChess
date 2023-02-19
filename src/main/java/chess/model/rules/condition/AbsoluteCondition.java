package chess.model.rules.condition;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.board.Tile;
import chess.model.piece.PieceType;
import chess.model.piece.PieceTypeID;

public class AbsoluteCondition extends Condition<Coordinate> {
    public AbsoluteCondition() {
        super();
    }

    @Override
    public boolean verifyCondition(PieceTypeID ptid, Coordinate c, Board b) {
        for (PieceType p : condition.keySet()) {
            Tile tile = b.getBoard().get(condition.get(p));
            if (pieceTypeCases(tile, p, ptid).isPresent()) return pieceTypeCases(tile, p, ptid).get();
        }
        return false;
    }
}
