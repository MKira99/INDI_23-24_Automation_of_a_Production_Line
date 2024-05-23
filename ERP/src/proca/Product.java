package proca;

public class Product {
    private String productId;
    private int quantity;

    // Constructor
    public Product(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to calculate the total cost of the product based on the price and quantity
    //public double getTotalCost() {
     //   return price * quantity;
    //}
}

