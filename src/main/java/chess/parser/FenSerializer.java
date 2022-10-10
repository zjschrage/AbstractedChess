package chess.parser;

import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.board.Dimension;
import chess.model.board.Tile;
import chess.model.piece.Piece;
import chess.model.piece.Player;

public class FenSerializer {

    private Dimension dimension;

    public FenSerializer(Dimension dimension) {
        this.dimension = dimension;
    }

    public String getFen(Board b) {
        String fen = "";
        for (int row = dimension.y() - 1; row >= 0; row--) {
            int spaceCount = 0;
            for (int col = 0; col < dimension.x(); col++) {
                Piece p = b.getBoard().get(new Coordinate(col, row)).getPiece();
                if (p == null) {
                    spaceCount++;
                    continue;
                }
                else {
                    char id = (char)p.getTID().id;
                    if (p.getTID().playerId == Player.BLACK) id += Constants.ASCII_CASE_OFFSET;
                    if (spaceCount > 0) fen += spaceCount;
                    fen += id;
                    spaceCount = 0;
                }
            }
            if (spaceCount > 0) fen += spaceCount;
            fen += Constants.FEN_DELIMETER;
        }
        fen = fen.substring(0, fen.length()-1);
        return fen;
    }

}
