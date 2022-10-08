package model.play;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.board.Board;
import model.board.Coordinate;
import model.piece.Piece;
import parser.Parser;

public class MoveNotation {

    public static final int ASCII_OFFSET = 97;

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
        for (Piece p : pieces) {
            if (prefix.contains("" + p.getTID().symbol)) {
                //String distinguisher = prefix.substring(1);
                return p.getCoordinate();
            }
        }
        for (Piece p : pieces) {
            if (p.getTID().symbol == '\0') {
                return p.getCoordinate();
            }
        }
        return new Coordinate(0, 0);
    }

    private boolean outOfTurn(Piece p) {
        return (PlayManager.turn % 2 != p.getTID().playerId.ordinal());
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        try {
            parser.loadGameFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Board b = parser.generateBoard();
        MoveNotation mover = new MoveNotation(b);
        String[] moves = {"e4", "e5", "Na3", "Na6", "Nf4", "Kh8", "Ra1"};
        for (String s : moves) {
            System.out.println(mover.translateNotation(s));
        }
    }

}

class IntRef {
    private int i;

    public IntRef(int i) { this.i = i; }
    public int val() { return i; }
    public void dec() { i--; }
}
