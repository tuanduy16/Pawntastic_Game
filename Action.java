//Action class that demonstrates a move
public class Action {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private boolean promotion; // determine whether we win or not

    //constructor for the Action class, when we haven't yet won
    public Action(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.promotion = false;
    }

    //constructor for the Action class, when we made a winning move
    public Action(int fromRow, int fromCol, int toRow, int toCol, boolean promotion) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.promotion = promotion;
    }

    //getters and setters and toString method 

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToCol() {
        return toCol;
    }

    public int getToRow() {
        return toRow;
    }

    public boolean isPromotion() {
        return promotion;
    }
}
