package model.rules;

import java.util.List;

import model.board.Board;
import model.board.Coordinate;

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
            default -> false;
        };
    }

    private boolean verifyTimesMoved(Coordinate c, Board b) {
        return (b.getBoard().get(c).getPiece().getTimesMoved() == Integer.parseInt(args.get(0)));
    }

    public String toString() {
        return "[" + propertyType.toString() + " " + args.toString() + "]";
    }

}
