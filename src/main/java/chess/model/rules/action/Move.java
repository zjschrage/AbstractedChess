package chess.model.rules.action;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;
import chess.model.play.PlayManager;
import chess.model.rules.MovementPattern;

import java.util.List;
import java.util.Optional;

public class Move extends Action {

    public Move(List<Object> args) {
        super(args);
    }

    @Override
    public void execute(Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return;
        MovementPattern from = MovementPattern.class.cast(args.get(0));
        MovementPattern to = MovementPattern.class.cast(args.get(1));
        Coordinate fromCord = new Coordinate(c.x() + from.xVector(), c.y() + from.yVector());
        Coordinate toCord = new Coordinate(c.x() + to.xVector(), c.y() + to.yVector());
        Optional<Piece> capturedPiece = Optional.ofNullable(b.getBoard().get(toCord).getPiece());
        Piece movPiece = getPiece(fromCord, b);
        if (movPiece == null || !b.checkInBounds(toCord)) return;
        movPiece.updateStatistics(toCord, PlayManager.turn, capturedPiece);
        b.getBoard().get(toCord).placePiece(b.getBoard().get(fromCord).pickUpPiece());
    }
}
