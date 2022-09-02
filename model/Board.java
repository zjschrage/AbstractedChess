package model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Coordinate, Tile> board;

    public Board() {
        board = new HashMap<Coordinate, Tile>();
    }

    public void initBoard(int x, int y, Map<Piece, Coordinate> pieces) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board.put(new Coordinate(i, j), new Tile());
            }
        }
        for (Piece p : pieces.keySet()) {
            board.put(pieces.get(p), new Tile(p));
        }
    }

    public void move(Coordinate from, Coordinate to) {

    }
}
