package chess.view;

import chess.model.board.Board;
import chess.model.board.Dimension;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JFrame {

    BoardPanel panel;

    public BoardView(Assets a, Board b, Dimension d) {
        new JFrame();
        panel = new BoardPanel(a, b, d);
        add(panel);
        setSize(d.x(), d.y());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        show();
    }

}
