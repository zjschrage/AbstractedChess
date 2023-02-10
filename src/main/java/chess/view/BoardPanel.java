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
        marginSize = new Dimension(d.x()/10, d.y()/10);
        tileSize = Math.min((d.x() - 2*marginSize.x())/b.getDimension().x(), (d.y() - 2*marginSize.y())/b.getDimension().y());
        pieceSize = (int)(tileSize * PIECE_TILE_SCALE);
        loadPieceImageAssets();
        generateBackground();
        generatePieceViews();
        generateTileViews();
        pm = new PlayManager(b);
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
        for (int i = 0; i < b.getDimension().y(); i++) {
            for (int j = 0; j < b.getDimension().x(); j++) {
                TileView tv;
                if ((i + j) % 2 == 1) tv = new TileView(TILE_A_COLOR, tileSize);
                else tv = new TileView(TILE_B_COLOR, tileSize);
                tv.setLocation(tileSize * i + marginSize.x(), tileSize * (b.getDimension().y() - 1 - j) + marginSize.y());
                tv.setSize(tileSize, tileSize);
                add(tv);
                setLayer(tv, -1);
                tileViewMap.put(new Coordinate(i, j), tv);
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
        for (int i = 0; i < b.getDimension().y(); i++) {
            for (int j = 0; j < b.getDimension().x(); j++) {
                Piece p = getPieceAtCoordinate(new Coordinate(i, j));
                if (p != null) {
                    PieceView pv = new PieceView(p, this, pieceImageMap.get(getCharFromPiece(p)));
                    pv.setLocation(tileSize * i + marginSize.x() + (tileSize - pieceSize)/2, tileSize * (b.getDimension().y() - 1 - j) + marginSize.y() + (tileSize - pieceSize)/2);
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
        for (int i = 0; i < b.getDimension().y(); i++) {
            for (int j = 0; j < b.getDimension().x(); j++) {
                Piece p = getPieceAtCoordinate(new Coordinate(i, j));
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
