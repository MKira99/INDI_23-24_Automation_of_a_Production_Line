package org.example;

public class Supplier {
    private int supplierId;
    private int[] prices=new int[2];
    private int shippingTime;

    // Constructor
    public Supplier(int supplierId, int[] prices, int shippingTime) {
        this.supplierId = supplierId;
        this.prices[0] = prices[0];
        this.prices[1] = prices[1];
        this.shippingTime=shippingTime;
    }

    // Getters and Setters
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public int getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(int shippingTime) {
        this.shippingTime = shippingTime;
    }
}

