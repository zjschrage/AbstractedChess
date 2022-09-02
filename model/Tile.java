package model;

import java.util.Optional;

public class Tile {
    Optional<Piece> piece;

    public Tile() {
        piece = Optional.ofNullable(null);
    }

    public Tile(Piece p) {
        piece = Optional.ofNullable(p);
    }
}
