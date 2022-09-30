package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import model.board.Coordinate;
import model.board.Dimension;
import model.piece.Piece;
import model.piece.PieceBehavior;

public class FenParser {

    public static final Character FEN_DELIMETER = '/';

    private Map<Character, PieceBehavior> pieceTable;

    public FenParser(Map<Character, PieceBehavior> pieceTable) {
        this.pieceTable = pieceTable;
    }

    public Dimension getDimensions(String fen) {
        String[] rows = fen.split("" + FEN_DELIMETER);
        return new Dimension(rows.length, getWidth(rows[0]));
    }

    public Map<Piece, Coordinate> configurePieces(String fen) {
        Map<Piece, Coordinate> pieceConfiguration = new HashMap<>();
        String[] rows = fen.split("" + FEN_DELIMETER);
        for (int i = 0; i < rows.length; i++) {
            for (SimpleEntry<Piece, Coordinate> e : processRow(rows[i], rows.length - 1 - i)) {
                pieceConfiguration.put(e.getKey(), e.getValue());
            }
        }
        return pieceConfiguration;
    }

    private List<SimpleEntry<Piece, Coordinate>> processRow(String s, int row) {
        List<SimpleEntry<Piece, Coordinate>> piecesOnRow = new ArrayList<>();
        int column = 0;
        for (Character c : s.toCharArray()) {
            if (Character.isDigit(c)) column += Character.getNumericValue(c);
            else {
                Coordinate cord = new Coordinate(column, row);
                PieceBehavior b = pieceTable.get(c);
                piecesOnRow.add(new SimpleEntry<Piece,Coordinate>(new Piece(b, cord), cord));
                column++;
            }
        }
        return piecesOnRow;
    }

    private int getWidth(String s) {
        int width = 0;
        for (Character c : s.toCharArray()) {
            if (Character.isDigit(c)) width += Character.getNumericValue(c);
            else width++;
        }
        return width;
    }
    
}
