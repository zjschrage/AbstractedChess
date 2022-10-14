package chess;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

public class AppTest {

    public static String SHORT_CASTLE = "O-O";
    public static String LONG_CASTLE = "O-O-O";
    public static Parser parser;
    public static Board b;
    public static PlayManager pm;

    @BeforeClass
    public static void globalInit() {
        parser = new Parser();
        try {
            parser.loadGameFile(Launcher.GAME_RESOURCE_FOLDER + "Chess.txt");
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
        String ans = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR";
        pm.move("e4");
        assertTrue(ans.equals(b.getFEN()));
    }

    @Test
    public void game2() {
        String ans = "rnbqkbnr/ppp1pppp/8/3p4/3P1B2/8/PPP1PPPP/RN1QKBNR";
        pm.move("d4");
        pm.move("d5");
        pm.move("Bf4");
        assertTrue(ans.equals(b.getFEN()));
    }

    @Test
    public void game3() {
        pm.move("d4");
        pm.move("d5");
        pm.move("Bf4");
        boolean badMove = pm.move("d4");
        assertFalse(badMove);
    }

    @Test
    public void zjschrage_VS_kb1113() {
        String fen = "8/8/8/8/p7/3K4/1k6/8";
        String game = "1. d4 d5 2. c4 e5 3. dxe5 d4 4. e3 Bb4+ 5. Bd2 dxe3 6. fxe3 Bxd2+ 7. Qxd2 Qxd2+ " +
                "8. Nxd2 Ne7 9. c5 Nec6 10. Bb5 O-O 11. Ngf3 a6 12. Ba4 Nb4 13. Ke2 Bg4 14. a3 " +
                "Nd5 15. h3 Bxf3+ 16. gxf3 c6 17. Bc2 Nd7 18. Nc4 Nxc5 19. Rad1 Rad8 20. Rhg1 " +
                "Rfe8 21. f4 Nc7 22. Nd6 Rf8 23. Nf5 g6 24. Ne7+ Kg7 25. f5 Nd5 26. h4 Rfe8 27. " +
                "h5 Nxe7 28. f6+ Kf8 29. Rxd8 Rxd8 30. fxe7+ Kxe7 31. hxg6 hxg6 32. b4 Nd7 33. " +
                "Rg5 Nf8 34. Kf3 Rd2 35. Rg2 Rxg2 36. Kxg2 Ke6 37. Bb3+ Kxe5 38. Bxf7 Ke4 39. Kf2 " +
                "Kd3 40. Kf3 b6 41. e4 c5 42. bxc5 bxc5 43. e5 c4 44. e6 Nxe6 45. Bxe6 c3 46. Bf7 " +
                "g5 47. Kg4 c2 48. Bg6+ Kc3 49. Bxc2 Kxc2 50. Kxg5 Kb3 51. Kf4 Kxa3 52. Ke4 Kb3 " +
                "53. Kd3 a5 54. Kd2 Kb2 55. Kd3 a4";
        verify(game, fen);
    }

    @Test
    public void zjschrage_VS_BLESS12MASH() {
        String fen = "8/7p/5bp1/4pk1P/8/5P2/3r2PK/8";
        String game = "1. d4 d5 2. Bf4 Nf6 3. e3 g6 4. Nf3 Bg7 5. Bd3 O-O 6. Nbd2 e6 7. Ne5 Nbd7 8. h4 " +
                "c5 9. Bg5 cxd4 10. exd4 Qb6 11. Bxf6 Nxf6 12. Nd7 Nxd7 13. b3 Qxd4 14. O-O Qxa1 " +
                "15. Nf3 Qxd1 16. Rxd1 Nf6 17. Bb5 a6 18. Bd3 b5 19. Nd4 e5 20. Nc6 Bb7 21. Nxe5 " +
                "Ne4 22. Nd7 Rfd8 23. Bxe4 Rxd7 24. Bf3 Rad8 25. Re1 d4 26. Bg4 Rd6 27. Re7 Bd5 " +
                "28. Ra7 Be6 29. Bf3 d3 30. cxd3 Rxd3 31. Rxa6 Rd2 32. Rb6 Rxa2 33. Rxb5 Rb2 34. " +
                "Rb6 Rxb3 35. Rxb3 Bxb3 36. Kh2 Be6 37. Bg4 Rd2 38. Bxe6 fxe6 39. f3 e5 40. Kg3 " +
                "Kf7 41. Kg4 Ke6 42. Kg3 Kf5 43. h5 Bf6 44. Kh2 ";
        verify(game, fen);
    }

    @Test
    public void GMBenjaminFinegold_VS_McFattal() {
        String fen = "r7/3k2p1/1p1p4/2p1r3/2P1p3/P3P1P1/1P1N1PKP/R2R4";
        String game = "1. d4 Nf6 2. c4 e5 3. dxe5 Ne4 4. Qd5 b6 5. Qxe4 Bb4+ 6. Bd2 Bxd2+ 7. Nxd2 Nc6 " +
                "8. Ngf3 Bb7 9. a3 Qe7 10. g3 O-O-O 11. Bg2 Rhe8 12. O-O f6 13. Qxh7 Nxe5 14. " +
                "Nxe5 Bxg2 15. Kxg2 fxe5 16. Qe4 d6 17. Qa8+ Kd7 18. Qxa7 Ra8 19. Qb7 e4 20. Qd5 " +
                "Qe5 21. Qxe5 Rxe5 22. e3 c5 23. Rfd1";
        verify(game, fen);
    }

    @Test
    public void Hikaru_VS_JustaTrickstar() {
        String fen = "1r3rk1/1ppq2p1/p2p3p/5b2/P1P2R1B/2Pn2P1/5QBP/5RK1";
        String game = "1. c4 e5 2. Nc3 Nf6 3. Nf3 Nc6 4. e3 Bb4 5. Qc2 Bxc3 6. bxc3 O-O 7. e4 d6 8. g3 " +
                "Be6 9. Nh4 Nd7 10. d3 a6 11. a4 Ne7 12. Bg2 Qc8 13. Rb1 Rb8 14. O-O f5 15. Bg5 " +
                "Nf6 16. exf5 Nxf5 17. Nxf5 Bxf5 18. f4 Ng4 19. Qd2 h6 20. Bh4 exf4 21. Rxf4 Ne5 " +
                "22. Rbf1 Qd7 23. Qf2 Nxd3";
        verify(game, fen);
    }

    @Test
    public void HansNeimann_VS_DominguezPerezLeinier() {
        String fen = "7R/2p3p1/1pbb1k2/2p2p2/2P2P2/1PN1K3/8/8";
        String game = "1. e4 e5 2. Nf3 Nc6 3. Bb5 Nf6 4. O-O Nxe4 5. d4 Nd6 6. Bxc6 dxc6 7. dxe5 Nf5 8. " +
                "Qxd8+ Kxd8 9. h3 Bd7 10. Rd1 Be7 11. Nc3 Ke8 12. g4 Nh4 13. Nxh4 Bxh4 14. Kg2 h5 " +
                "15. f3 hxg4 16. hxg4 f6 17. Bf4 f5 18. g5 Rh5 19. Ne2 Ke7 20. Rh1 Rah8 21. Rh3 " +
                "Bxg5 22. Rxh5 Rxh5 23. Rh1 Bxf4 24. Rxh5 Bxe5 25. b3 c5 26. c4 a5 27. Kf2 a4 28. " +
                "Ke3 axb3 29. axb3 Kf6 30. f4 Bd6 31. Nc3 Bc6 32. Rh8 b6 33. Rg8 Kf7 34. Rh8 Kf6 " +
                "35. Rg8 Kf7 36. Rh8 Kf6";
        verify(game, fen);
    }

    @Test
    public void MagnusCarlsen_VS_IanNepomniachi() {
        String fen = "3k4/5RN1/4P3/5P2/7K/8/8/6q1";
        String game = "1. d4 Nf6 2. Nf3 d5 3. g3 e6 4. Bg2 Be7 5. O-O " +
                "O-O 6. b3 c5 7. dxc5 Bxc5 8. c4 dxc4 9. Qc2 Qe7 10. " +
                "Nbd2 Nc6 11. Nxc4 b5 12. Nce5 Nb4 13. Qb2 Bb7 14. a3 " +
                "Nc6 15. Nd3 Bb6 16. Bg5 Rfd8 17. Bxf6 gxf6 18. Rac1 Nd4 19. " +
                "Nxd4 Bxd4 20. Qa2 Bxg2 21. Kxg2 Qb7+ 22. Kg1 Qe4 23. Qc2 " +
                "a5 24. Rfd1 Kg7 25. Rd2 Rac8 26. Qxc8 Rxc8 27. Rxc8 Qd5 28. " +
                "b4 a4 29. e3 Be5 30. h4 h5 31. Kh2 Bb2 32. Rc5 Qd6 33. Rd1 " +
                "Bxa3 34. Rxb5 Qd7 35. Rc5 e5 36. Rc2 Qd5 37. Rdd2 Qb3 38. " +
                "Ra2 e4 39. Nc5 Qxb4 40. Nxe4 Qb3 41. Rac2 Bf8 42. Nc5 Qb5 43. Nd3 " +
                "a3 44. Nf4 Qa5 45. Ra2 Bb4 46. Rd3 Kh6 47. Rd1 Qa4 48. " +
                "Rda1 Bd6 49. Kg1 Qb3 50. Ne2 Qd3 51. Nd4 Kh7 52. Kh2 " +
                "Qe4 53. Rxa3 Qxh4+ 54. Kg1 Qe4 55. Ra4 Be5 56. Ne2 Qc2 57. " +
                "R1a2 Qb3 58. Kg2 Qd5+ 59. f3 Qd1 60. f4 Bc7 61. Kf2 " +
                "Bb6 62. Ra1 Qb3 63. Re4 Kg7 64. Re8 f5 65. Raa8 Qb4 66. " +
                "Rac8 Ba5 67. Rc1 Bb6 68. Re5 Qb3 69. Re8 Qd5 70. Rcc8 Qh1 " +
                "71. Rc1 Qd5 72. Rb1 Ba7 73. Re7 Bc5 74. " +
                "Re5 Qd3 75. Rb7 Qc2 76. Rb5 Ba7 77. Ra5 Bb6 78. Rab5 Ba7 79. Rxf5 " +
                "Qd3 80. Rxf7+ Kxf7 81. Rb7+ Kg6 82. Rxa7 Qd5 83. " +
                "Ra6+ Kh7 84. Ra1 Kg6 85. Nd4 Qb7 86. Ra2 Qh1 87. Ra6+ Kf7 " +
                "88. Nf3 Qb1 89. Rd6 Kg7 90. Rd5 Qa2+ 91. " +
                "Rd2 Qb1 92. Re2 Qb6 93. Rc2 Qb1 94. Nd4 Qh1 95. Rc7+ Kf6 96. Rc6+ " +
                "Kf7 97. Nf3 Qb1 98. Ng5+ Kg7 99. Ne6+ Kf7 100. " +
                "Nd4 Qh1 101. Rc7+ Kf6 102. Nf3 Qb1 103. Rd7 Qb2+ 104. Rd2 Qb1 105. " +
                "Ng1 Qb4 106. Rd1 Qb3 107. Rd6+ Kg7 108. Rd4 Qb2+ " +
                "109. Ne2 Qb1 110. e4 Qh1 111. Rd7+ Kg8 112. Rd4 Qh2+ 113. " +
                "Ke3 h4 114. gxh4 Qh3+ 115. Kd2 Qxh4 116. Rd3 Kf8 117. " +
                "Rf3 Qd8+ 118. Ke3 Qa5 119. Kf2 Qa7+ 120. Re3 Qd7 121. Ng3 " +
                "Qd2+ 122. Kf3 Qd1+ 123. Re2 Qb3+ 124. Kg2 Qb7 125. " +
                "Rd2 Qb3 126. Rd5 Ke7 127. Re5+ Kf7 128. Rf5+ Ke8 129. e5 Qa2+ 130. " +
                "Kh3 Qe6 131. Kh4 Qh6+ 132. Nh5 Qh7 133. e6 Qg6 134. " +
                "Rf7 Kd8 135. f5 Qg1 136. Ng7";
        verify(game, fen);
    }

}
