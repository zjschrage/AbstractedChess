import java.io.IOException;
import java.util.Scanner;

import model.board.Board;
import model.board.Coordinate;
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
        b.printBoard();

        PlayManager pm = new PlayManager(b);
        Scanner scan = new Scanner(System.in);
        boolean run = true;
        while(run) {
            String[] arg = scan.nextLine().split(" ");
            if (arg[0].equals("g")) {
                Piece p = b.getBoard().get(new Coordinate(Integer.parseInt(arg[1]),Integer.parseInt(arg[2]))).getPiece();
                if (p != null) b.printBoard(p);
                else System.out.println("No Piece");
            }
            else {
                pm.move(new Coordinate(Integer.parseInt(arg[0]),Integer.parseInt(arg[1])), new Coordinate(Integer.parseInt(arg[2]),Integer.parseInt(arg[3])));
                b.printBoard();
            }
        }
        scan.close();
    }
}
