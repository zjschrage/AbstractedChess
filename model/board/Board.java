package model.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.piece.Piece;
import model.piece.Player;
import model.rules.Action;

public class Board {
    private Map<Coordinate, Tile> board;
    private Dimension dimension;

    public Board() {
        board = new HashMap<Coordinate, Tile>();
    }

    public void initBoard(Dimension d, Map<Piece, Coordinate> pieces) {
        this.dimension = d;
        for (int i = 0; i < d.x(); i++) {
            for (int j = 0; j < d.y(); j++) {
                board.put(new Coordinate(i, j), new Tile());
            }
        }
        for (Piece p : pieces.keySet()) {
            board.put(pieces.get(p), new Tile(p));
        }
    }

    public void printBoard() {
        for (int i = dimension.y() - 1; i >= 0; i--) {
            for (int j = 0; j < dimension.x(); j++) {
                Piece p = board.get(new Coordinate(j, i)).getPiece();
                if (p != null) {
                    if (p.getTID().playerId == Player.WHITE) System.out.print((char)p.getTID().id + " ");
                    else System.out.print((char)(p.getTID().id + 32) + " ");
                }
                else System.out.print("_ ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printBoard(Piece piece) {
        List<Coordinate> fm = piece.getFeasableMoves(this);
        for (int i = dimension.y() - 1; i >= 0; i--) {
            for (int j = 0; j < dimension.x(); j++) {
                Coordinate c = new Coordinate(j, i);
                if (fm.contains(c)) {
                    System.out.print("* ");
                    continue;
                }
                Piece p = board.get(c).getPiece();
                if (p != null) {
                    if (p.getTID().playerId == Player.WHITE) System.out.print((char)p.getTID().id + " ");
                    else System.out.print((char)(p.getTID().id + 32) + " ");
                }
                else System.out.print("_ ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Map<Coordinate, Tile> getBoard() {
        return board;
    }

    public boolean checkInBounds(Coordinate c) {
        return (c.x() >= 0 && c.x() < dimension.x() && c.y() >= 0 && c.y() < dimension.y());
    }

    public void move(Coordinate from, Coordinate to, List<Action> actions) {

    }
}
