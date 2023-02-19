package chess.model.rules.condition;

import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.board.Tile;
import chess.model.piece.PieceType;
import chess.model.piece.PieceTypeID;
import chess.model.rules.MovementPattern;
import chess.model.rules.property.Property;

public abstract class Condition<T> {
    
//    private Map<PieceType, Coordinate> absoluteCondition;
//    private Map<PieceType, MovementPattern> relativeCondition;
//    private Map<PieceType, Property> propertyCondition;
    protected Map<PieceType, T> condition;

//    public Condition(Map<PieceType, Coordinate> absoluteCondition, Map<PieceType, MovementPattern> relativeCondition, Map<PieceType, Property> propertyCondition) {
//        this.absoluteCondition = absoluteCondition;
//        this.relativeCondition = relativeCondition;
//        this.propertyCondition = propertyCondition;
//    }

    public Condition() {
        this.condition = new HashMap<>();
    }

    public void addSubCondition(SimpleEntry<PieceType, T> condition) {
        this.condition.put(condition.getKey(), condition.getValue());
    }


//    public void addAbsoluteCondition(SimpleEntry<PieceType, Coordinate> absoluteCondition) {
//        this.absoluteCondition.put(absoluteCondition.getKey(), absoluteCondition.getValue());
//    }
//
//    public void addRelativeCondition(SimpleEntry<PieceType, MovementPattern> relativeCondition) {
//        this.relativeCondition.put(relativeCondition.getKey(), relativeCondition.getValue());
//    }
//
//    public void addPropertyCondition(SimpleEntry<PieceType, Property> propertyCondition) {
//        this.propertyCondition.put(propertyCondition.getKey(), propertyCondition.getValue());
//    }

//    public boolean verifyCondition(PieceTypeID pt, Coordinate c, Board b) {
//        return verifyAbsoluteCondition(pt, b) || verifyRelativeCondition(pt, c, b) || verifyPropertyCondition(pt, c, b);
//    }

    public abstract boolean verifyCondition(PieceTypeID ptid, Coordinate c, Board b);

//    private boolean verifyAbsoluteCondition(PieceTypeID ptid, Board b) {
//        for (PieceType p : absoluteCondition.keySet()) {
//            Tile tile = b.getBoard().get(absoluteCondition.get(p));
//            if (pieceTypeCases(tile, p, ptid).isPresent()) return pieceTypeCases(tile, p, ptid).get();
//        }
//        return false;
//    }

//    private boolean verifyRelativeCondition(PieceTypeID ptid, Coordinate c, Board b) {
//        for (PieceType p : relativeCondition.keySet()) {
//            MovementPattern relVec = relativeCondition.get(p);
//            Coordinate relPos = new Coordinate(c.x() + relVec.xVector(), c.y() + relVec.yVector());
//            Tile tile = b.getBoard().get(relPos);
//            if (pieceTypeCases(tile, p, ptid).isPresent()) return pieceTypeCases(tile, p, ptid).get();
//        }
//        return false;
//    }

//    private boolean verifyPropertyCondition(PieceTypeID pt, Coordinate c, Board b) {
//        for (PieceType p : propertyCondition.keySet()) {
//            if (propertyCondition.get(p).verifyProperty(p, c, b)) return true;
//        }
//        return false;
//    }

    protected Optional<Boolean> pieceTypeCases(Tile tile, PieceType p, PieceTypeID ptid) {
        if (tile == null || tile.getPiece() == null) return Optional.of(false);
        if (p.all) return Optional.of(true);
        if (p.friendly && tile.getPiece().getTID().playerId == ptid.playerId) return Optional.of(true);
        if (p.enemy && tile.getPiece().getTID().playerId != ptid.playerId) return Optional.of(true);
        if (tile.getPiece().getTID().id == p.type) return Optional.of(true);
        return Optional.empty();
    }

//    private <V> void mapPrinter(Map<PieceType, V> map) {
//        for (PieceType k : map.keySet()) {
//            System.out.printf("%s\n%s\n", k.toString(), map.get(k).toString());
//        }
//        System.out.println();
//    }
//
//    public void print() {
//        mapPrinter(absoluteCondition);
//        mapPrinter(relativeCondition);
//        mapPrinter(propertyCondition);
//    }

}
