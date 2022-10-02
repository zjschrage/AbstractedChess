import java.io.IOException;
import java.util.Scanner;

import model.board.Board;
import model.board.Coordinate;
import model.piece.Piece;
import model.piece.Player;
import model.play.PlayManager;
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

        PlayManager pm = new PlayManager(b);
        Scanner scan = new Scanner(System.in);
        boolean run = true;
        while(run) {
            String[] arg = scan.nextLine().split(" ");
            pm.move(new Coordinate(Integer.parseInt(arg[0]),Integer.parseInt(arg[1])), new Coordinate(Integer.parseInt(arg[2]),Integer.parseInt(arg[3])));
            b.printBoard();
        }
        scan.close();
    }
}

// for (int i = 0; i < 8; i++) {
//     for (int j = 0; j < 8; j++) {
//         Piece p = b.getBoard().get(new Coordinate(j,i)).getPiece();
//         if (p == null) continue;
//         if (p.getTID().playerId == Player.WHITE) System.out.print((char)p.getTID().id);
//         else System.out.print((char)(p.getTID().id + 32));
//         System.out.println(":\n");
//         b.printBoard(p);
//     }
// }