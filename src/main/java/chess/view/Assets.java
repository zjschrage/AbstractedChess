package chess.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static chess.view.ImageLoader.IMG_RESOURCE_FOLDER;

public class Assets {

    private Map<Integer, BufferedImage> spriteSheetMap;
    private Map<Character, BufferedImage> pieceMap;

    public Assets() {
        spriteSheetMap = new HashMap<>();
        pieceMap = new HashMap<>();
    }

    public void loadSpriteSheet(String path, int id) {
        try {
            BufferedImage b = ImageIO.read(ImageLoader.class.getResource(IMG_RESOURCE_FOLDER + path));
            spriteSheetMap.put(id, b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImage(Character c, int id, int x, int y, int width, int height) {
        pieceMap.put(c, crop(id, x, y, width, height));
    }

    public BufferedImage getImage(Character c) {
        return pieceMap.get(c);
    }

    private BufferedImage crop(int id, int x, int y, int width, int height) {
        return spriteSheetMap.get(id).getSubimage(x, y, width, height);
    }

}
