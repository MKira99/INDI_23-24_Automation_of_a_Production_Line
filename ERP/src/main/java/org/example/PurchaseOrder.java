package org.example;
import java.util.Date;

public class PurchaseOrder {
    private int poNumber;
    private Date orderDate;
    private Supplier supplier;
    private int piece;
    private int quantity;
    private double totalCost;

    // Constructor
    public PurchaseOrder(Supplier supplier, int piece,int quantity) {
        this.supplier = supplier;
        this.piece=piece;
        this.quantity=quantity;
        this.totalCost=supplier.getPrices()[piece-1]*quantity;
    }



    // Getters and Setters
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}

