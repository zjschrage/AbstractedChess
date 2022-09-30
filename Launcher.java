import java.io.IOException;

import model.board.Board;
import model.board.Coordinate;
import model.piece.Piece;
import model.piece.Player;
import parser.Parser;

public class Launcher {
    public static void main(String[] args) {

        Parser parser = new Parser();
        try {
            parser.loadGameFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Board b = parser.generateBoard();
        b.printBoard();
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = b.getBoard().get(new Coordinate(j,i)).getPiece();
                if (p == null) continue;
                if (p.getTID().playerId == Player.WHITE) System.out.print((char)p.getTID().id);
                else System.out.print((char)(p.getTID().id + 32));
                System.out.println(":\n");
                b.printBoard(p);
            }
        }
    }
}