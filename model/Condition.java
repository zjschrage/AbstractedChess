package model;

import java.util.Map;

public class Condition {
    Map<Piece, Coordinate> absoluteCondition;
    Map<Piece, MovementPattern> relativeCondition;
    Map<Piece, Property> propertyCondition;

    public Condition(Map<Piece, Coordinate> absoluteCondition, Map<Piece, MovementPattern> relativeCondition, Map<Piece, Property> propertyCondition) {
        this.absoluteCondition = absoluteCondition;
        this.relativeCondition = relativeCondition;
        this.propertyCondition = propertyCondition;
    }

    public boolean verifyCondition(Coordinate c, Board b, ConditionType t) {
        return verifyAbsoluteCondition(b, t) && verifyRelativeCondition(c, b, t);
    }

    private boolean verifyAbsoluteCondition(Board b, ConditionType t) {
        boolean retVal = (t == ConditionType.FULFILL);
        for (Piece p : absoluteCondition.keySet()) {
            Tile tile = b.getBoard().get(absoluteCondition.get(p));
            if (tile == null || tile.getPiece() == null) {
                if (!retVal) continue;
                else return !retVal;
            }
            if (!tile.getPiece().equals(p)) return !retVal;
        }
        return retVal;
    }

    private boolean verifyRelativeCondition(Coordinate c, Board b, ConditionType t) {
        boolean retVal = (t == ConditionType.FULFILL);
        for (Piece p : relativeCondition.keySet()) {
            MovementPattern relVec = relativeCondition.get(p);
            Coordinate relPos = new Coordinate(c.x() + relVec.xVector(), c.y() + relVec.yVector());
            Tile tile = b.getBoard().get(relPos);
            if (tile == null || tile.getPiece() == null) {
                if (!retVal) continue;
                else return !retVal;
            }
            if (!tile.getPiece().equals(p)) return !retVal;
        }
        return retVal;
    }

}
