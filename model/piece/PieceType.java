package model.piece;

import model.rules.MovementPattern;

public class PieceType {

    //Comparison Qualifiers
    public int type;                          // num
    public boolean all;                       // *
    public boolean friendly;                  // +
    public boolean enemy;                     // -
    public boolean selfInstance;              // / (use for reflexively checking properties)  
    public MovementPattern relativeNeighbor;  // (check neighbors properties, NOT checking own properties)

    public PieceType() {
        this.type = 0;
        this.all = false;
        this.friendly = false;
        this.enemy = false;
        this.selfInstance = false;
        this.relativeNeighbor = null;
    }

    public String toString() {
        return String.format("[type=%d, all=%b, friendly=%b, enemy= %b, self= %b, neighbor=%s]", type, all, friendly, enemy, selfInstance, relativeNeighbor);
    }

     
    
}
