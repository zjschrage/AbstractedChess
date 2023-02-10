package chess.view;

import chess.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImageLoader {

    public static final String IMG_RESOURCE_FOLDER = "/img/";
    public static final String VIEW_CONFIG = "viewConfig.txt";

    private Assets a;

    public ImageLoader(Assets a) {
        this.a = a;
        try {
            readViewConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void readViewConfig() throws IOException {
        InputStream is = ImageLoader.class.getResourceAsStream(IMG_RESOURCE_FOLDER + VIEW_CONFIG);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() == 0) continue;
            if (line.charAt(0) == 's') addSpriteSheet(line);
            else if (line.charAt(0) == 'a') addImageFromSheet(line);
        }
        br.close();
    }

    private void addSpriteSheet(String line) {
        String[] cont = line.split("\\s+");
        a.loadSpriteSheet(cont[1], stoi(cont[2]));
    }

    private void addImageFromSheet(String line) {
        String[] cont = line.split("\\s+");
        a.addImage(cont[2].charAt(0), stoi(cont[1]), stoi(cont[3]), stoi(cont[4]), stoi(cont[5]), stoi(cont[6]));
    }

    private int stoi(String s) {
        return Integer.parseInt(s);
    }

}
