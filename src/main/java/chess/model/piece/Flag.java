package chess.model.piece;

public class Flag {

    private int value;
    private int birth;
    private int lifespan;

    public Flag(int value, int birth, int lifespan) {
        this.value = value;
        this.birth = birth;
        this.lifespan = lifespan;
    }

    public boolean isActive(int time) {
        return (time - birth <= lifespan);
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return String.format("[value=%d, birth=%d, lifespan=%d]", value, birth, lifespan);
    }
    
}
