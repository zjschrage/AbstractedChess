package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<Coordinate, Tile> board;
    private int xDimension;
    private int yDimension;

    public Board() {
        board = new HashMap<Coordinate, Tile>();
    }

    public void initBoard(int x, int y, Map<Piece, Coordinate> pieces) {
        this.xDimension = x;
        this.yDimension = y;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board.put(new Coordinate(i, j), new Tile());
            }
        }
        for (Piece p : pieces.keySet()) {
            board.put(pieces.get(p), new Tile(p));
        }
    }

    public void printBoard() {
        for (int i = 0; i < xDimension; i++) {
            for (int j = 0; j < yDimension; j++) {
                Piece p = board.get(new Coordinate(i, j)).getPiece();
                if (p != null) System.out.print(p.toString() + " ");
                else System.out.print("_ ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Map<Coordinate, Tile> getBoard() {
        return board;
    }

    public int getXDimension() {
        return xDimension;
    }

    public int getYDimension() {
        return yDimension;
    }

    public boolean checkInBounds(Coordinate c) {
        return (c.x() >= 0 && c.x() < xDimension && c.y() >= 0 && c.y() < yDimension);
    }

    public void move(Coordinate from, Coordinate to, List<Action> actions) {

    }
}
