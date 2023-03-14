package chess.model.rules.condition;

import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.board.Tile;
import chess.model.piece.PieceType;
import chess.model.piece.PieceTypeID;

public abstract class Condition<T> {

    protected Map<PieceType, T> condition;

    public Condition() {
        this.condition = new HashMap<>();
    }

    public void addSubCondition(SimpleEntry<PieceType, T> condition) {
        this.condition.put(condition.getKey(), condition.getValue());
    }

    public abstract boolean verifyCondition(PieceTypeID ptid, Coordinate c, Board b);

    protected Optional<Boolean> pieceTypeCases(Tile tile, PieceType p, PieceTypeID ptid) {
        if (tile == null || tile.getPiece() == null) return Optional.of(false);
        if (p.all) return Optional.of(true);
        if (p.friendly && tile.getPiece().getTID().playerId == ptid.playerId) return Optional.of(true);
        if (p.enemy && tile.getPiece().getTID().playerId != ptid.playerId) return Optional.of(true);
        if (tile.getPiece().getTID().id == p.type) return Optional.of(true);
        return Optional.empty();
    }

    public String toString() {
        String str = "";
        for (PieceType pt : condition.keySet()) {
            str += pt + " " + condition.get(pt) + "\n";
        }
        return str;
    }


}
