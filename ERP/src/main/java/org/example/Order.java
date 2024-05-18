package org.example;

public class Order {
    private String clientName;
    private int orderNumber;
    private String workpiece;
    private int quantity;
    private int dueDate;
    private int latePen;
    private int earlyPen;

    public Order(String clientName, int orderNumber, String workpiece, int quantity, int dueDate, int latePen, int earlyPen) {
        this.clientName = clientName;
        this.orderNumber = orderNumber;
        this.workpiece = workpiece;
        this.quantity = quantity;
        this.dueDate = dueDate;
        this.latePen = latePen;
        this.earlyPen = earlyPen;
    }

    public String getClientName() {
        return clientName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getWorkpiece() {
        return workpiece;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDueDate() {
        return dueDate;
    }

    public int getLatePen() {
        return latePen;
    }

    public int getEarlyPen() {
        return earlyPen;
    }

    @Override
    public String toString() {
        return "Order{" +
                "clientName='" + clientName + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", workpiece='" + workpiece + '\'' +
                ", quantity='" + quantity + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", latePen='" + latePen + '\'' +
                ", earlyPen='" + earlyPen + '\'' +
                '}';
    }
}