package Interface;

public class OrderDetails {
    private int orderID;
    private int pieceProduced;
    private int totalTime;

    public OrderDetails(int orderID, int pieceProduced) {
        this.orderID = orderID;
        this.pieceProduced = pieceProduced;
        //this.totalTime = totalTime;
    }

    // Create getters for the fields

    public int getOrderID() {
        return orderID;
    }

    public int getPieceProduced() {
        return pieceProduced;
    }

    public int getTotalTime() {
        return totalTime;
    }
}

