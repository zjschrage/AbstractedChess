package chess.model.play;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;

public class MoveNotation {

    public static final int ASCII_OFFSET = 97;

    public static final Character CHECK = '+';
    public static final Character CHECK_MATE = '#';

    private Board board;
    private Set<Character> pieceIndicators;

    MoveNotation(Board board) {
        this.board = board;
        pieceIndicators = new HashSet<>();
        for (Coordinate c : board.getBoard().keySet()) {
            Piece p = board.getBoard().get(c).getPiece();
            if (p == null) continue;
            pieceIndicators.add(p.getTID().symbol);
        }
    }
    
    public Move translateNotation(String notation) {
        notation = notation.trim();
        char last = notation.charAt(notation.length()-1);
        if (last == CHECK || last == CHECK_MATE) notation = notation.substring(0, notation.length()-1);
        return getMove(notation);
    }

    public Coordinate translateCoordinate(String notation) {
        return getDst(notation, new IntRef(notation.length()-1));
    }

    private Move getMove(String notation) {
        IntRef i = new IntRef(notation.length() - 1);
        Coordinate dst = getDst(notation, i);
        List<Piece> pieces = getPotentialPieces(dst);
        if (pieces.size() == 0) return null;
        if (pieces.size() == 1) return new Move(pieces.get(0).getCoordinate(), dst);
        String prefix = notation.substring(0, i.val());
        Coordinate src = determinePiece(pieces, prefix);
        return new Move(src, dst);
    }

    private Coordinate getDst(String notation, IntRef i) {
        int x = -1;
        int y = -1;
        while (i.val() >= 0) {
            if (!Character.isDigit(notation.charAt(i.val()))) {
                x = notation.charAt(i.val()) - ASCII_OFFSET;
                y = Integer.parseInt(notation.substring(i.val()+1)) - 1;
                break;
            }
            i.dec();
        }
        return new Coordinate(x, y);
    }

    private List<Piece> getPotentialPieces(Coordinate dst) {
        List<Piece> pieces = new ArrayList<>();
        for (Coordinate c : board.getBoard().keySet()) {
            Piece p = board.getBoard().get(c).getPiece();
            if (p == null) continue;
            if (outOfTurn(p)) continue;
            Set<Coordinate> cords = p.getFeasableMoves(board).keySet();
            if (cords.contains(dst)) pieces.add(p);
        }
        return pieces;
    }

    private Coordinate determinePiece(List<Piece> pieces, String prefix) {
        List<Piece> narrowedPieces = new ArrayList<>();
        for (Piece p : pieces) {
            if (prefix.contains("" + p.getTID().symbol)) {
                narrowedPieces.add(p);
            }
        }
        if (narrowedPieces.size() == 0) {
            for (Piece p : pieces) {
                if (p.getTID().symbol == '\0') {
                    narrowedPieces.add(p);
                }
            }
        }
        if (narrowedPieces.size() == 1) return narrowedPieces.get(0).getCoordinate();
        for (Piece p : narrowedPieces) {
            char distinguisher = (p.getTID().symbol == '\0') ? prefix.charAt(0) : prefix.charAt(1);
            if (!coordinateHasDistinguisher(p.getCoordinate(), distinguisher)) continue;
            return p.getCoordinate();
        }
        return new Coordinate(0, 0);
    }

    private boolean outOfTurn(Piece p) {
        return (PlayManager.turn % 2 != p.getTID().playerId.ordinal());
    }

    private boolean coordinateHasDistinguisher(Coordinate c, char distinguisher) {
        int val = 0;
        if (!Character.isDigit(distinguisher)) {
            val = (int)distinguisher - ASCII_OFFSET;
            return (c.x() == val);
        }
        else {
            val = Integer.parseInt("" + distinguisher) - 1;
            return (c.y() == val);
        }
    }

}

class IntRef {
    private int i;

    public IntRef(int i) { this.i = i; }
    public int val() { return i; }
    public void dec() { i--; }
}
