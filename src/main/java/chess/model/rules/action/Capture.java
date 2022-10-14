package chess.model.rules.action;

import chess.model.board.Board;
import chess.model.board.Coordinate;

import java.util.List;

public class Capture extends Action {

    public Capture(ActionType actionType, List<Object> args) {
        super(actionType, args);
    }

    @Override
    public void execute(Coordinate c, Board b) {

    }

}
