package model;

public class MovementPattern {
    private int xVector;
    private int yVector;
    private int repetitions;

    public MovementPattern(int xVector, int yVector, int repetitions) {
        this.xVector = xVector;
        this.yVector = yVector;
        this.repetitions = repetitions;
    }

    public int getXVec() {
        return xVector;
    }
}
