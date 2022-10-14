package chess.model.play;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.Piece;
import chess.model.piece.Player;

import java.util.Optional;

public class PlayManager {

    //Global Turn Variable
    public static int turn;
    
    private Board board;
    private MoveNotation moveNotator;

    public PlayManager(Board board) {
        turn = 0;
        this.board = board;
        this.moveNotator = new MoveNotation(board);
    }

    public boolean move(String notation) {
        Move mv = moveNotator.translateNotation(notation);
        if (mv != null) return move(mv.src(), mv.dst());
        return false;
    }

    public boolean move(Coordinate from, Coordinate to) {
        Piece p = board.getBoard().get(from).getPiece();
        if (p == null || !verifyOrder(p) || !verifyNoSelfCheck(p)) return false;
        Optional<Piece> capturedPiece = Optional.ofNullable(board.getBoard().get(to).getPiece());
        if (board.move(from, to)) {
            p.updateStatistics(to, turn, capturedPiece);
            turn++;
            return true;
        }
        return false;
    }

    public Coordinate translateCoordinate(String notation) {
        return moveNotator.translateCoordinate(notation);
    }

    private boolean verifyOrder(Piece p) {
        Player hasTurn = (turn % 2 == 0) ? Player.WHITE : Player.BLACK;
        return (p.getTID().playerId == hasTurn);
    }

    public boolean verifyNoSelfCheck(Piece p) {
        //Removing piece from old position allows a check -> return false
        //Piece in new position allows a check -> return false
        return true;
    }

}
