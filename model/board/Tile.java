package model.board;

import model.piece.Piece;

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
}
