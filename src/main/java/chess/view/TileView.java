package chess.view;

import chess.model.board.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TileView extends JLabel {

    private BufferedImage image;
    private BufferedImage selected;
    private ImageIcon imageIcon;
    private ImageIcon selectedIcon;
    public TileView(Color color, int tileSize) {
        super();
        image = genImage(tileSize, color);
        selected = genImage(tileSize, Color.CYAN);
        imageIcon = new ImageIcon(image);
        selectedIcon = new ImageIcon(selected);
        setIcon(imageIcon);
    }

    public void select() {
        setIcon(selectedIcon);
    }

    public void deselect() {
        setIcon(imageIcon);
    }

    private BufferedImage genImage(int tileSize, Color color) {
        BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < tileSize; i++) {
            for (int j = 0; j < tileSize; j++) {
                image.setRGB(i, j, color.getRGB());
            }
        }
        return image;
    }
}
