package org.example;

public class OrderInformation {
    private int piece1Quantity;
    private int piece2Quantity;

    public OrderInformation(int piece1Quantity, int piece2Quantity) {
        this.piece1Quantity = piece1Quantity;
        this.piece2Quantity = piece2Quantity;
    }

    public int getPiece1Quantity() {
        return piece1Quantity;
    }

    public void setPiece1Quantity(int piece1Quantity) {
        this.piece1Quantity = piece1Quantity;
    }

    public int getPiece2Quantity() {
        return piece2Quantity;
    }

    public void setPiece2Quantity(int piece2Quantity) {
        this.piece2Quantity = piece2Quantity;
    }
}

