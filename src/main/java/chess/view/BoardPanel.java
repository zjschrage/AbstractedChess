package chess.view;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.board.Dimension;
import chess.model.board.Tile;
import chess.model.piece.Piece;
import chess.model.piece.Player;
import chess.model.play.PlayManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.util.Map;

import static java.awt.Image.SCALE_DEFAULT;

public class BoardPanel extends JLayeredPane {

    private static int ASCII_OFFSET = 32;
    private static double PIECE_TILE_SCALE = 0.85;
    private static Color TILE_A_COLOR = new Color(230, 138, 0);
    private static Color TILE_B_COLOR = new Color(255, 255, 230);
    private static double DEFAULT_MARGIN_PROPORTION = 0.2;

    private Assets a;
    private Board b;
    private Dimension boardSize;
    private Dimension marginSize;
    private int tileSize;
    private int pieceSize;
    private Map<Coordinate, TileView> tileViewMap;
    private Map<Piece, PieceView> pieceViewMap;
    private Map<Character, BufferedImage> pieceImageMap;
    private PlayManager pm;

    public BoardPanel(Assets a, Board b, Dimension d) {
        this.a = a;
        this.b = b;
        this.boardSize = d;
        tileSize = (int)(Math.min(d.x()/b.getDimension().x(), d.y()/b.getDimension().y()) * visualScale(b.getDimension()));
        marginSize = new Dimension((d.x()-tileSize*b.getDimension().y())/2, (d.y()-tileSize*b.getDimension().x())/2);
        pieceSize = (int)(tileSize * PIECE_TILE_SCALE);
        loadPieceImageAssets();
        generateBackground();
        generatePieceViews();
        generateTileViews();
        pm = new PlayManager(b);
    }

    private double visualScale(Dimension d) {
        double r = ((double)Math.abs(d.x() - d.y()))/Math.max(d.x(), d.y());
        //0 -> (1 - default margin proportion)
        //1 -> (0: no margin proportion)
        return (1 - DEFAULT_MARGIN_PROPORTION) + (Math.log10(10*r + 1) * DEFAULT_MARGIN_PROPORTION);
    }

    public void checkMovement(Piece piece, Coordinate mouse) {
        for (Coordinate c : piece.getFeasibleMoves(b).keySet()) {
            if (tileViewMap.get(c).getBounds().contains(mouse.x(), mouse.y())) {
                if (pm.move(piece.getCoordinate(), c)) break;
            }
        }
        clearPieceViews();
        generatePieceViews();
    }

    public void illuminateFeasible(Piece piece) {
        for (Coordinate c : piece.getFeasibleMoves(b).keySet()) {
            tileViewMap.get(c).select();
        }
    }

    public void deluminate() {
        for (Coordinate c : tileViewMap.keySet()) {
            tileViewMap.get(c).deselect();
        }
    }

    private void generateTileViews() {
        tileViewMap = new HashMap<>();
        for (int i = 0; i < b.getDimension().x(); i++) { //row
            for (int j = 0; j < b.getDimension().y(); j++) { //col
                TileView tv;
                Coordinate c = new Coordinate(j,i);
                if ((i + j) % 2 == 1) tv = new TileView(TILE_A_COLOR, tileSize);
                else tv = new TileView(TILE_B_COLOR, tileSize);
                tv.setLocation(tileSize * j + marginSize.x(), tileSize * (b.getDimension().x() - 1 - i) + marginSize.y());
                tv.setSize(tileSize, tileSize);
                add(tv);
                setLayer(tv, -1);
                tileViewMap.put(c, tv);
            }
        }
    }

    private Piece getPieceAtCoordinate(Coordinate c) {
        Tile t = b.getBoard().get(c);
        if (t == null) return null;
        Piece p = t.getPiece();
        return p;
    }

    private Character getCharFromPiece(Piece p) {
        if (p.getTID().playerId == Player.BLACK) return (char)(p.getTID().id + ASCII_OFFSET);
        return (char)(p.getTID().id);
    }

    private void generatePieceViews() {
        pieceViewMap = new HashMap<>();
        for (int i = 0; i < b.getDimension().x(); i++) {
            for (int j = 0; j < b.getDimension().y(); j++) {
                Piece p = getPieceAtCoordinate(new Coordinate(j, i)); //col row
                if (p != null) {
                    PieceView pv = new PieceView(p, this, pieceImageMap.get(getCharFromPiece(p)));
                    pv.setLocation(tileSize * j + marginSize.x() + (tileSize - pieceSize)/2, tileSize * (b.getDimension().x() - 1 - i) + marginSize.y() + (tileSize - pieceSize)/2);
                    pv.setSize(pieceSize, pieceSize);
                    add(pv);
                    setLayer(pv, 0);
                    pieceViewMap.put(p, pv);
                }
            }
        }
    }

    private void clearPieceViews() {
        for (Piece p : pieceViewMap.keySet()) {
            remove(pieceViewMap.get(p));
        }
    }

    private void loadPieceImageAssets() {
        pieceImageMap = new HashMap<>();
        for (int i = 0; i < b.getDimension().x(); i++) { //row
            for (int j = 0; j < b.getDimension().y(); j++) { //col
                Piece p = getPieceAtCoordinate(new Coordinate(j, i)); //(col row)
                if (p != null) {
                    char c = getCharFromPiece(p);
                    BufferedImage image = a.getImage(c);
                    image = imgTobufImg(image.getScaledInstance(pieceSize, pieceSize, SCALE_DEFAULT));
                    pieceImageMap.put(c, image);
                }
            }
        }
    }

    private BufferedImage imgTobufImg(Image img) {
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    private void generateBackground() {
        int backgroundSize = Math.max(boardSize.x(), boardSize.y());
        TileView bv = new TileView(Color.BLACK, backgroundSize);
        bv.setLocation(0, 0);
        bv.setSize(backgroundSize, backgroundSize);
        add(bv);
        setLayer(bv, -2);
    }

}
