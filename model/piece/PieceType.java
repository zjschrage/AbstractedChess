package model.piece;

public class PieceType {

    //Comparison Qualifiers
    public int type;                // num
    public boolean all;             // *
    public boolean friendly;        // +
    public boolean enemy;           // -
    public boolean selfInstance;    // / (use for reflexively checking properties)  

    public PieceType(int type, boolean all, boolean friendly, boolean enemy, boolean selfInstance) {
        this.type = type;
        this.all = all;
        this.friendly = friendly;
        this.enemy = enemy;
        this.selfInstance = selfInstance;
    }

    public String toString() {
        return "[" + type + " " + all + " " + friendly + " " + enemy + " " + selfInstance + "]";
    }

     
    
}
