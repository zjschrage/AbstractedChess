package chess.model.board;

import chess.model.piece.Piece;

public class Tile {

    private Piece piece;

    public Tile() {
        piece = null;
    }

    public Tile(Piece p) {
        piece = p;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece pickUpPiece() {
        Piece p = piece;
        piece = null;
        return p;
    }

    public void placePiece(Piece p) {
        piece = p;
    }
}
