package model.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.piece.Piece;
import model.rules.Action;

public class Board {

    private Map<Coordinate, Tile> board;
    private Dimension dimension;
    private BoardPrinter boardPrinter;

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
        boardPrinter = new BoardPrinter(board, dimension);
    }

    public Map<Coordinate, Tile> getBoard() {
        return board;
    }

    public boolean checkInBounds(Coordinate c) {
        return (c.x() >= 0 && c.x() < dimension.x() && c.y() >= 0 && c.y() < dimension.y());
    }

    public boolean move(Coordinate from, Coordinate to) {
        Piece p = board.get(from).getPiece();
        Map<Coordinate, List<Action>> feasableMoves = p.getFeasableMoves(this);
        if (!feasableMoves.containsKey(to)) return false;
        board.get(to).placePiece(board.get(from).pickUpPiece());
        List<Action> actions = feasableMoves.get(to);
        if (actions == null) return true;
        for (Action a : actions) {
            a.execute(to, this);
        }
        return true;
    }

    public void print() {
        boardPrinter.printBoard();
    }

    public void print(Piece piece) {
        boardPrinter.printBoard(piece, this);
    }
}
