package chess.model.piece;

public class PieceTypeID {
    
    public Player playerId;
    public int id;
    public char symbol;

    public PieceTypeID(Player playerId, int id, char symbol) {
        this.playerId = playerId;
        this.id = id;
        this.symbol = symbol;
    }

}