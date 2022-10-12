package chess.model.rules.action;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Flag;
import chess.model.piece.Piece;
import chess.model.play.PlayManager;

import java.util.List;

public class SetFlag extends Action {

    public SetFlag(ActionType actionType, List<Object> args) {
        super(actionType, args);
    }

    @Override
    public void execute(Coordinate c, Board b) {
        Piece p = getPiece(c, b);
        if (p == null) return;
        p.getFlags().put(Integer.parseInt((String)args.get(0)), new Flag(Integer.parseInt((String)args.get(1)), PlayManager.turn, Integer.parseInt((String)args.get(2))));
    }
}
