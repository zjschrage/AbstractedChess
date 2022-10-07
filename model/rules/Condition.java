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

    public boolean verifyCondition(PieceTypeID pt, Coordinate c, Board b) {
        return verifyAbsoluteCondition(pt, b) || verifyRelativeCondition(pt, c, b) || verifyPropertyCondition(pt, c, b);
    }

    private boolean verifyAbsoluteCondition(PieceTypeID pt, Board b) {
        for (PieceType p : absoluteCondition.keySet()) {
            Tile tile = b.getBoard().get(absoluteCondition.get(p));
            if (tile == null || tile.getPiece() == null) return false;
            if (p.all) return true;
            if (p.friendly && tile.getPiece().getTID().playerId == pt.playerId) return true;
            if (p.enemy && tile.getPiece().getTID().playerId != pt.playerId) return true;
            if (tile.getPiece().getTID().id == p.type) return true;
        }
        return false;
    }

    private boolean verifyRelativeCondition(PieceTypeID pt, Coordinate c, Board b) {
        for (PieceType p : relativeCondition.keySet()) {
            MovementPattern relVec = relativeCondition.get(p);
            Coordinate relPos = new Coordinate(c.x() + relVec.xVector(), c.y() + relVec.yVector());
            Tile tile = b.getBoard().get(relPos);
            if (tile == null || tile.getPiece() == null) return false;
            if (p.all) return true;
            if (p.friendly && tile.getPiece().getTID().playerId == pt.playerId) return true;
            if (p.enemy && tile.getPiece().getTID().playerId != pt.playerId) return true;
            if (tile.getPiece().getTID().id == p.type) return true;
        }
        return false;
    }

    private boolean verifyPropertyCondition(PieceTypeID pt, Coordinate c, Board b) {
        for (PieceType p : propertyCondition.keySet()) {
            if (propertyCondition.get(p).verifyProperty(p, c, b)) return true;
        }
        return false;
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
