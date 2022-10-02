package model.rules;

import java.util.List;

import model.board.Board;
import model.board.Coordinate;
import model.piece.Piece;
import model.play.PlayManager;

public class Property {

    private PropertyType propertyType;
    private List<String> args;
    
    public Property(PropertyType propertyType, List<String> args) {
        this.propertyType = propertyType;
        this.args = args;
    }

    public boolean verifyProperty(Coordinate c, Board b) {
        return switch (propertyType) {
            case TIMES_MOVED -> verifyTimesMoved(c, b);
            case TURNS_AGO_MOVED -> verifyLastTurnMoved(c, b);
            default -> false;
        };
    }

    private boolean verifyTimesMoved(Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        return (p != null && p.getTimesMoved() == Integer.parseInt(args.get(0)));
    }

    private boolean verifyLastTurnMoved(Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        return (p != null && p.getLastTurnMoved() + Integer.parseInt(args.get(0)) == PlayManager.turn);
    }

    private Piece getPiece(Coordinate c, Board b) {
        if (!b.checkInBounds(c)) return null;
        else return b.getBoard().get(c).getPiece();
    }

    public String toString() {
        return "[" + propertyType.toString() + " " + args.toString() + "]";
    }

}
