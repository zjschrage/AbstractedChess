package model.rules;

import java.util.List;

import model.board.Board;
import model.board.Coordinate;
import model.piece.Flag;
import model.piece.Piece;
import model.piece.PieceType;
import model.play.PlayManager;

public class Property {

    private PropertyType propertyType;
    private List<Object> args;
    
    public Property(PropertyType propertyType, List<Object> args) {
        this.propertyType = propertyType;
        this.args = args;
    }

    public boolean verifyProperty(PieceType pt, Coordinate c, Board b) {
        return switch (propertyType) {
            case TIMES_MOVED -> verifyTimesMoved(pt, c, b);
            case TURNS_AGO_MOVED -> verifyLastTurnMoved(pt, c, b);
            case CHECK_FLAG -> verifyFlag(pt, c, b);
            default -> false;
        };
    }

    private boolean verifyTimesMoved(PieceType pt, Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return false;
        if (pt.relativeNeighbor != null) {
            Coordinate targetCord = new Coordinate(c.x() + pt.relativeNeighbor.xVector(), c.y() + pt.relativeNeighbor.yVector());
            Piece target = getPiece(targetCord, b);
            if (target == null) return false;
            if (pt.friendly && p.getTID().playerId != target.getTID().playerId) return false;
            if (pt.enemy && p.getTID().playerId == target.getTID().playerId) return false;
            if (pt.type == target.getTID().id) return false;
            return (target.getTimesMoved() == Integer.parseInt((String)args.get(0)));
        }
        return (p.getTimesMoved() == Integer.parseInt((String)args.get(0)));
    }

    private boolean verifyLastTurnMoved(PieceType pt, Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return false;
        if (pt.relativeNeighbor != null) {
            Coordinate targetCord = new Coordinate(c.x() + pt.relativeNeighbor.xVector(), c.y() + pt.relativeNeighbor.yVector());
            Piece target = getPiece(targetCord, b);
            if (target == null) return false;
            if (pt.friendly && p.getTID().playerId != target.getTID().playerId) return false;
            if (pt.enemy && p.getTID().playerId == target.getTID().playerId) return false;
            if (pt.type == target.getTID().id) return false;
            return ((target.getLastTurnMoved() + Integer.parseInt((String)args.get(0))) == PlayManager.turn);
        }
        return ((p.getLastTurnMoved() + Integer.parseInt((String)args.get(0))) == PlayManager.turn);
    }

    private boolean verifyFlag(PieceType pt, Coordinate c, Board b) {
        if (pt.relativeNeighbor == null) return false;
        c = new Coordinate(c.x() + pt.relativeNeighbor.xVector(), c.y() + pt.relativeNeighbor.yVector());
        Piece p = getPiece(c, b);
        if (p == null) return false;
        Flag f = p.getFlags().get(Integer.parseInt((String)args.get(0)));
        if (f == null) return false;
        if (!f.isActive(PlayManager.turn)) return false;
        return (f.getValue() == Integer.parseInt((String)args.get(1)));
    }

    private Piece getPiece(Coordinate c, Board b) {
        if (!b.checkInBounds(c)) return null;
        else return b.getBoard().get(c).getPiece();
    }

    public String toString() {
        return String.format("[type=%s, args=%s]", propertyType.toString(), args.toString());
    }

}
