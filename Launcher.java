import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;

public class Launcher {
    public static void main(String[] args) {
        Board b = new Board();
        Map<Piece, Coordinate> initialState = new HashMap<>();
        Map<MovementPattern, Condition> condition = new HashMap<>();
        Map<MovementPattern, Action> action = new HashMap<>();
        List<MovementPattern> mp = new ArrayList<>();
        mp.add(new MovementPattern(0, 1, -1));
        mp.add(new MovementPattern(0, -1, -1));
        Piece p = new Piece(0, mp, condition, action, new Coordinate(4, 4));
        initialState.put(p, new Coordinate(4, 4));

        Piece p2 = new Piece(0, mp, condition, action, new Coordinate(4, 6));
        initialState.put(p2, new Coordinate(4, 6));

        b.initBoard(8, 8, initialState);
        b.printBoard();

        for (Coordinate c : p.getFeasableMoves(b)) {
            System.out.println(c.toString());
        }
    }
}