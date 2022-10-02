package model.piece;

import model.rules.MovementPattern;

public class PieceType {

    //Comparison Qualifiers
    public int type;                          // num
    public boolean all;                       // *
    public boolean friendly;                  // +
    public boolean enemy;                     // -
    public boolean selfInstance;              // / (use for reflexively checking properties)  
    public MovementPattern relativeNeighbor;  // (check neighbors properties)

    public PieceType(int type, boolean all, boolean friendly, boolean enemy, boolean selfInstance, MovementPattern relativeNeighbor) {
        this.type = type;
        this.all = all;
        this.friendly = friendly;
        this.enemy = enemy;
        this.selfInstance = selfInstance;
        this.relativeNeighbor = relativeNeighbor;
    }

    public String toString() {
        return "[" + type + " " + all + " " + friendly + " " + enemy + " " + selfInstance + " " + relativeNeighbor + "]";
    }

     
    
}
