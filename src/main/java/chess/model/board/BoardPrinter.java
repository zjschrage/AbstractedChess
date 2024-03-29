package chess.model.board;

import java.util.Map;
import java.util.Set;

import chess.model.piece.Piece;
import chess.model.piece.Player;
import chess.parser.Constants;

public class BoardPrinter {

    private Map<Coordinate, Tile> boardObject;
    private Dimension dimension;

    public BoardPrinter(Map<Coordinate, Tile> boardObject, Dimension dimension) {
        this.boardObject = boardObject;
        this.dimension = dimension;
    }

    public void printBoard() {
        for (int i = dimension.x() - 1; i >= 0; i--) {
            System.out.printf("%d--  ", i+1);
            for (int j = 0; j < dimension.y(); j++) {
                Piece p = boardObject.get(new Coordinate(j, i)).getPiece();
                if (p != null) {
                    if (p.getTID().playerId == Player.WHITE) System.out.print((char)p.getTID().id + " ");
                    else System.out.print((char)(p.getTID().id + Constants.ASCII_CASE_OFFSET) + " ");
                }
                else System.out.print("_ ");
            }
            System.out.println();
        }
        footer();
    }

    public void printBoard(Piece piece, Board board) {
        Set<Coordinate> fm = piece.getFeasibleMoves(board).keySet();
        for (int i = dimension.x() - 1; i >= 0; i--) {
            System.out.printf("%d--  ", i+1);
            for (int j = 0; j < dimension.y(); j++) {
                Coordinate c = new Coordinate(j, i);
                if (fm.contains(c)) {
                    System.out.print("* ");
                    continue;
                }
                Piece p = boardObject.get(c).getPiece();
                if (p != null) {
                    if (p.getTID().playerId == Player.WHITE) System.out.print((char)p.getTID().id + " ");
                    else System.out.print((char)(p.getTID().id + 32) + " ");
                }
                else System.out.print("_ ");
            }
            System.out.println();
        }
        footer();
    }

    private void footer() {
        System.out.print("\n     ");
        for (int j = 0; j < dimension.y(); j++) {
            System.out.print("| ");
        }
        System.out.print("\n     ");
        for (int j = 0; j < dimension.y(); j++) {
            System.out.printf("%c ", (char)(j + 97));
        }
        System.out.println();
    }

}
