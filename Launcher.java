import java.io.IOException;
import java.util.Scanner;

import model.board.Board;
import model.piece.Piece;
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
        b.print();

        PlayManager pm = new PlayManager(b);
        Scanner scan = new Scanner(System.in);
        boolean run = true;
        while(run) {
            String[] arg = scan.nextLine().split(" ");
            if (arg[0].equals("g")) {
                Piece p = b.getBoard().get(pm.translateCoordinate(arg[1])).getPiece();
                if (p != null) b.print(p);
                else System.out.println("No Piece");
            }
            else {
                pm.move(arg[0]);
                b.print();
            }
        }
        scan.close();
    }
}
