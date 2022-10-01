package model.rules;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import model.board.Board;
import model.board.Coordinate;
import model.board.Tile;
import model.piece.PieceType;
import model.piece.PieceTypeID;

public class Condition {
    
    private Map<PieceType, Coordinate> absoluteCondition;
    private Map<PieceType, MovementPattern> relativeCondition;
    private Map<PieceType, Property> propertyCondition;

    public Condition(Map<PieceType, Coordinate> absoluteCondition, Map<PieceType, MovementPattern> relativeCondition, Map<PieceType, Property> propertyCondition) {
        this.absoluteCondition = absoluteCondition;
        this.relativeCondition = relativeCondition;
        this.propertyCondition = propertyCondition;
    }

    public void addAbsoluteCondition(SimpleEntry<PieceType, Coordinate> absoluteCondition) {
        this.absoluteCondition.put(absoluteCondition.getKey(), absoluteCondition.getValue());
    }

    public void addRelativeCondition(SimpleEntry<PieceType, MovementPattern> relativeCondition) {
        this.relativeCondition.put(relativeCondition.getKey(), relativeCondition.getValue());
    }

    public void addPropertyCondition(SimpleEntry<PieceType, Property> propertyCondition) {
        this.propertyCondition.put(propertyCondition.getKey(), propertyCondition.getValue());
    }

    public boolean verifyCondition(PieceTypeID pt, Coordinate c, Board b, ConditionType t) {
        if (t == ConditionType.FULFILL) return verifyAbsoluteCondition(pt, b, t) && verifyRelativeCondition(pt, c, b, t) && verifyPropertyCondition(pt, c, b, t);
        else return verifyAbsoluteCondition(pt, b, t) || verifyRelativeCondition(pt, c, b, t) || verifyPropertyCondition(pt, c, b, t);
    }

    private boolean verifyAbsoluteCondition(PieceTypeID pt, Board b, ConditionType t) {
        boolean retVal = (t == ConditionType.FULFILL);
        for (PieceType p : absoluteCondition.keySet()) {
            Tile tile = b.getBoard().get(absoluteCondition.get(p));
            if (tile == null || tile.getPiece() == null) {
                if (!retVal) continue;
                else return false;
            }
            if (p.all) {
                if (retVal) continue;
                else return true;
            }
            else if (p.friendly && tile.getPiece().getTID().playerId == pt.playerId) {
                if (retVal) continue;
                else return true;
            }
            else if (p.enemy && tile.getPiece().getTID().playerId != pt.playerId) {
                if (retVal) continue;
                else return true;
            }
            else if (tile.getPiece().getTID().id == p.type) {
                if (retVal) continue;
                else return true;
            }
        }
        return retVal;
    }

    private boolean verifyRelativeCondition(PieceTypeID pt, Coordinate c, Board b, ConditionType t) {
        boolean retVal = (t == ConditionType.FULFILL);
        for (PieceType p : relativeCondition.keySet()) {
            MovementPattern relVec = relativeCondition.get(p);
            Coordinate relPos = new Coordinate(c.x() + relVec.xVector(), c.y() + relVec.yVector());
            Tile tile = b.getBoard().get(relPos);
            if (tile == null || tile.getPiece() == null) {
                if (!retVal) continue;
                else return false;
            }
            if (p.all) {
                if (retVal) continue;
                else return true;
            }
            else if (p.friendly && tile.getPiece().getTID().playerId == pt.playerId) {
                if (retVal) continue;
                else return true;
            }
            else if (p.enemy && tile.getPiece().getTID().playerId != pt.playerId) {
                if (retVal) continue;
                else return true;
            }
            else if (tile.getPiece().getTID().id == p.type) {
                if (retVal) continue;
                else return true;
            }
            else {
                if (retVal) return false;
                else continue;
            }
        }
        return retVal;
    }

    private boolean verifyPropertyCondition(PieceTypeID pt, Coordinate c, Board b, ConditionType t) {
        boolean retVal = (t == ConditionType.FULFILL);
        for (PieceType p : propertyCondition.keySet()) {
            if (propertyCondition.get(p).verifyProperty(c, b)) {
                if (retVal) continue;
                else return true;
            }
            else {
                if (retVal) return false;
                else continue;
            }
        }
        return retVal;
    }

    public void print() {
        mapPrinter(absoluteCondition);
        mapPrinter(relativeCondition);
        mapPrinter(propertyCondition);
    }

    private <V> void mapPrinter(Map<PieceType, V> map) {
        System.out.println("MAP:");
        for (PieceType k : map.keySet()) {
            System.out.print(k.toString());
            System.out.println(" " + map.get(k).toString());
        }
        System.out.println();
    }

}
