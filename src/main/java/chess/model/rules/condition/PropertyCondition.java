package chess.model.rules.condition;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.PieceType;
import chess.model.piece.PieceTypeID;
import chess.model.rules.property.Property;

import java.util.Map;

public class PropertyCondition extends Condition<Property> {
    public PropertyCondition() {
        super();
    }

    @Override
    public boolean verifyCondition(PieceTypeID ptid, Coordinate c, Board b) {
        for (PieceType p : condition.keySet()) {
            if (condition.get(p).verifyProperty(p, c, b)) return true;
        }
        return false;
    }
}
