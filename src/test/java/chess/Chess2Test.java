package chess;

import chess.model.board.Board;
import chess.model.play.PlayManager;
import chess.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Chess2Test {

    public static String SHORT_CASTLE = "O-O";
    public static String LONG_CASTLE = "O-O-O";
    public static Parser parser;
    public static Board b;
    public static PlayManager pm;

    @BeforeClass
    public static void globalInit() {
        parser = new Parser();
        try {
            parser.loadGameFile(Launcher.GAME_RESOURCE_FOLDER + "Chess2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void init() {
        b = parser.generateBoard();
        pm = new PlayManager(b);
    }

    @After
    public void print() {
        b.print();
        System.out.println(b.getFEN());
    }

    private void verify(String game, String fen) {
        List<String> gameStream = Arrays.asList(game.split(" ")).stream()
                .filter(s -> !s.contains("."))
                .toList();
        for (int i = 0; i < gameStream.size(); i++) {
            if (gameStream.get(i).equals(SHORT_CASTLE)) {
                if (i % 2 == 0) pm.move("Kg1");
                else pm.move("Kg8");
            }
            else if (gameStream.get(i).equals(LONG_CASTLE)) {
                if (i % 2 == 0) pm.move("Kc1");
                else pm.move("Kc8");
            }
            else pm.move(gameStream.get(i));

        }
        assertTrue(fen.equals(b.getFEN()));
    }

    @Test
    public void game1() {
        String ans = "rnbcsqkscbnr/pppppppppppp/12/12/12/4P7/12/PPPP1PPPPPPP/RNBCSQKSCBNR";
        pm.move("e4");
        assertTrue(ans.equals(b.getFEN()));
    }

    @Test
    public void game2() {
        String ans = "rnb1cqk1cbnr/pppppppp1spp/8p3/9p2/12/5P6/4C1S2NP1/PPPPP1PPPPBP/RNB1SQK4R";
        pm.move("Ce3");
        pm.move("Sf7");
        pm.move("k3");
        pm.move("j6");
        pm.move("f4");
        pm.move("i7");
        pm.move("Bk2");
        pm.move("Sj8");
        pm.move("Cij3");
        pm.move("Sxj3");
        pm.move("Nxj3");
        pm.move("Ce9");
        pm.move("Shg3");
        assertTrue(ans.equals(b.getFEN()));
    }

}
