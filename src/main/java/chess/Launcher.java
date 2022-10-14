package chess;
import java.io.IOException;
import java.util.Scanner;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.play.PlayManager;
import chess.parser.Parser;

public class Launcher {

    public static final String GAME_RESOURCE_FOLDER = "/game/";

    public static void main(String[] args) {

        Parser parser = new Parser();
        try {
            parser.loadGameFile(GAME_RESOURCE_FOLDER + "Chess.txt");
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
