package chess.model.rules.action;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;
import chess.model.rules.MovementPattern;

import java.util.List;

public class Remove extends Action {

    public Remove(List<Object> args) {
        super(args);
    }

    @Override
    public void execute(Coordinate c, Board b) {
        MovementPattern vec = MovementPattern.class.cast(args.get(0));
        Coordinate rem = new Coordinate(c.x() + vec.xVector(), c.y() + vec.yVector());
        Piece remPiece = getPiece(rem, b);
        if (remPiece == null) return;
        b.getBoard().get(rem).pickUpPiece();
    }

}
