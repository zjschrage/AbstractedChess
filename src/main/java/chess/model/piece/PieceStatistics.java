package chess.model.piece;

import chess.model.board.Coordinate;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PieceStatistics {

    private Coordinate coordinate;
    private int timesMoved;
    private int lastTurnNumberMoved;
    private Map<Integer, Piece> captureList;

    public PieceStatistics(Coordinate coordinate) {
        this.coordinate = coordinate;
        timesMoved = 0;
        lastTurnNumberMoved = -1;
        captureList = new HashMap<>();
    }

    public void updateStatistics(Coordinate c, int turn, Optional<Piece> p) {
        coordinate = c;
        timesMoved++;
        lastTurnNumberMoved = turn;
        p.ifPresent(piece -> captureList.put(turn, piece));
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getTimesMoved() {
        return timesMoved;
    }

    public int getLastTurnNumberMoved() {
        return lastTurnNumberMoved;
    }

    public Map<Integer, Piece> getCaptureList() {
        return captureList;
    }

}
