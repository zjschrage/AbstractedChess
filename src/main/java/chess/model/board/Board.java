package chess.model.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.model.piece.Piece;
import chess.model.rules.action.Action;
import chess.parser.FenSerializer;

public class Board {

    private Map<Coordinate, Tile> board;
    private Dimension dimension;
    private BoardPrinter boardPrinter;
    private FenSerializer fenSerializer;

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
        fenSerializer = new FenSerializer(dimension);
    }

    public Map<Coordinate, Tile> getBoard() {
        return board;
    }

    public boolean checkInBounds(Coordinate c) {
        return (c.x() >= 0 && c.x() < dimension.x() && c.y() >= 0 && c.y() < dimension.y());
    }

    public boolean move(Coordinate from, Coordinate to) {
        Piece p = board.get(from).getPiece();
        Map<Coordinate, List<Action>> feasibleMoves = p.getFeasibleMoves(this);
        if (!feasibleMoves.containsKey(to)) return false;
        List<Action> actions = new ArrayList<>();
        if (board.get(to).getPiece() != null) actions.addAll(p.getCaptureActions(this));
        board.get(to).placePiece(board.get(from).pickUpPiece());
        List<Action> moveActions = feasibleMoves.get(to);
        if (moveActions != null) actions.addAll(moveActions);
        if (actions.size() == 0) return true;
        for (Action a : actions) {
            a.execute(to, this);
        }
        return true;
    }

    public String getFEN() {
        return fenSerializer.getFen(this);
    }

    public void print() {
        boardPrinter.printBoard();
    }

    public void print(Piece piece) {
        boardPrinter.printBoard(piece, this);
    }
}
