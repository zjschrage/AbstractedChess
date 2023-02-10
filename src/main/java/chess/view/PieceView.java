package chess.view;

import chess.model.board.Coordinate;
import chess.model.piece.Piece;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PieceView extends JLabel {

    private Coordinate offset;

    public PieceView(Piece piece, BoardPanel boardPanel, BufferedImage image) {
        super();
        ImageIcon icon = new ImageIcon(image);
        setIcon(icon);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                offset = new Coordinate(e.getXOnScreen() - getX(), e.getYOnScreen() - getY());
                boardPanel.illuminateFeasible(piece);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boardPanel.deluminate();
                Coordinate c = new Coordinate(e.getXOnScreen() - offset.x() + getWidth()/2, e.getYOnScreen() - offset.y() + getHeight()/2);
                boardPanel.checkMovement(piece, c);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - offset.x(), e.getYOnScreen() - offset.y());
            }
        });
    }

}
