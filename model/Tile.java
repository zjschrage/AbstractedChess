package model;

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
