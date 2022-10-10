package chess.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import chess.model.board.Coordinate;
import chess.model.board.Dimension;
import chess.model.piece.Piece;
import chess.model.piece.PieceBehavior;

import static chess.parser.Constants.FEN_DELIMETER;

public class FenParser {

    private Map<Character, PieceBehavior> pieceTable;

    /**
     * Accepts a mapping of characters representing the user defined piece tokens
     * to parsed and constructed piece behaviors to be used in piece instantiation.
     * This lets the parser know which pieces get what behavior when parsing the FEN
     * @param pieceTable Piece token to behavior mapping
     */
    public FenParser(Map<Character, PieceBehavior> pieceTable) {
        this.pieceTable = pieceTable;
    }

    /**
     * Gives the length and width of a board through a Dimension object
     * by determining the structure of the FEN notation
     * @param fen String notation for board configuration
     * @return Dimension object
     */
    public Dimension getDimensions(String fen) {
        String[] rows = fen.split("" + FEN_DELIMETER);
        return new Dimension(rows.length, getWidth(rows[0]));
    }

    /**
     * Creates instances of Piece objects and maps them to particular coordinates
     * from an input FEN notation string
     * @param fen string notation for board configuration
     * @return Piece instance to Coordinate mapping for board creation
     */
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
