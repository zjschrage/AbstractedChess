package model.play;

import model.board.Board;
import model.board.Coordinate;
import model.piece.Piece;
import model.piece.Player;

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

    public void move(String notation) {
        Move mv = moveNotator.translateNotation(notation);
        if (mv != null) move(mv.src(), mv.dst());
    }

    public void move(Coordinate from, Coordinate to) {
        Piece p = board.getBoard().get(from).getPiece();
        if (p == null || !verifyOrder(p) || !verifyNoSelfCheck(p)) return;
        if (board.move(from, to)) {
            p.updateStatistics(to, turn);
            turn++;
        }
    }

    public Coordinate translateCoordinate(String notation) {
        return moveNotator.translateCoordinate(notation);
    }

    private boolean verifyOrder(Piece p) {
        Player hasTurn = (turn % 2 == 0) ? Player.WHITE : Player.BLACK;
        System.out.println(turn + " " + hasTurn.toString());
        return (p.getTID().playerId == hasTurn);
    }

    public boolean verifyNoSelfCheck(Piece p) {
        //Removing piece from old position allows a check -> return false
        //Piece in new position allows a check -> return false
        return true;
    }

}
