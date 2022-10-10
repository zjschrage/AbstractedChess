package chess.model.rules;

public class MovementPattern {

    private int xVector;
    private int yVector;
    private int repetitions;

    public MovementPattern(int xVector, int yVector, int repetitions) {
        this.xVector = xVector;
        this.yVector = yVector;
        this.repetitions = repetitions;
    }

    public int xVector() {
        return xVector;
    }

    public int yVector() {
        return yVector;
    }

    public int repetitions() {
        return repetitions;
    }

    public String toString() {
        return String.format("[x=%d, y=%d, r=%d]", xVector, yVector, repetitions);
    }

}
