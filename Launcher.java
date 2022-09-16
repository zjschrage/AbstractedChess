import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;

public class Launcher {
    public static void main(String[] args) {
        Board b = new Board();
        Map<Piece, Coordinate> initialState = new HashMap<>();
            
            //PIECE
            Piece p = new Piece();
            Piece p2 = new Piece();

                //MOVEMENT PATTERN LIST
                List<MovementPattern> mp = new ArrayList<>();
                Map<MovementPattern, Condition> fCond = new HashMap<>();
                Map<MovementPattern, Condition> iCond = new HashMap<>();

                    //MOVEMENT
                    MovementPattern m1 = new MovementPattern(0, 1, -1);

                    //CONDITIONS
                    Map<Piece, Coordinate> absoluteConditionF = new HashMap<>();
                        //absoluteConditionF.put(p2, new Coordinate(1, 1));
                    Map<Piece, MovementPattern> relativeConditionF = new HashMap<>();
                    Map<Piece, Property> propertyConditionF = new HashMap<>();
                    fCond.put(m1, new Condition(absoluteConditionF, relativeConditionF, propertyConditionF));
                
                    Map<Piece, Coordinate> absoluteConditionI = new HashMap<>();
                        //absoluteConditionI.put(p2, new Coordinate(1, 1));
                    Map<Piece, MovementPattern> relativeConditionI = new HashMap<>();
                    Map<Piece, Property> propertyConditionI = new HashMap<>();
                    iCond.put(m1, new Condition(absoluteConditionI, relativeConditionI, propertyConditionI));

                    //MOVEMENT
                    MovementPattern m2 = new MovementPattern(0, -1, -1);

                    //CONDITIONS
                    Map<Piece, Coordinate> absoluteConditionF2 = new HashMap<>();
                        //absoluteConditionF2.put(p2, new Coordinate(1, 1));
                    Map<Piece, MovementPattern> relativeConditionF2 = new HashMap<>();
                    Map<Piece, Property> propertyConditionF2 = new HashMap<>();
                    fCond.put(m2, new Condition(absoluteConditionF2, relativeConditionF2, propertyConditionF2));
                    Map<Piece, Coordinate> absoluteConditionI2 = new HashMap<>();
                    Map<Piece, MovementPattern> relativeConditionI2 = new HashMap<>();
                    Map<Piece, Property> propertyConditionI2 = new HashMap<>();
                    iCond.put(m2, new Condition(absoluteConditionI2, relativeConditionI2, propertyConditionI2));
                
                mp.add(m1);
                mp.add(m2);

            p.init(Player.WHITE, mp, fCond, iCond, new Coordinate(4, 4));
            initialState.put(p, new Coordinate(4, 4));
        
            p2.init(Player.WHITE, mp, fCond, iCond, new Coordinate(4, 6));
            initialState.put(p2, new Coordinate(4, 6));

        
        b.initBoard(8, 8, initialState);
        b.printBoard();

        for (Coordinate c : p.getFeasableMoves(b)) {
            System.out.println(c.toString());
        }
    }
}