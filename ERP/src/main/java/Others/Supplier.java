package Others;

import java.util.HashMap;
import java.util.Map;

public class Supplier {
    private int supplierId;
    private String name;
    private String piece;
    private int minimumOrder;
    private double pricePerPiece;
    private int[] prices = new int[2];
    private int deliveryTime; // in days

    // Full constructor
    public Supplier(int supplierId, String name, String piece, int minimumOrder, double pricePerPiece, int deliveryTime) {
        this.supplierId = supplierId;
        this.name = name;
        this.piece = piece;
        this.minimumOrder = minimumOrder;
        this.pricePerPiece = pricePerPiece;
        this.deliveryTime = deliveryTime;
    }

    // Constructor for the price array
    public Supplier(int supplierId, int[] prices, int deliveryTime) {
        this.supplierId = supplierId;
        this.prices = prices;
        this.deliveryTime = deliveryTime;
    }

    // Getters and Setters
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public int getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(int minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public static Map<String, Supplier> suppliers = new HashMap<>();

    static {
        suppliers.put("SupplierA-P1", new Supplier(1, "SupplierA", "P1", 16, 30, 4));
        suppliers.put("SupplierA-P2", new Supplier(1, "SupplierA", "P2", 16, 10, 4));
        suppliers.put("SupplierB-P1", new Supplier(2, "SupplierB", "P1", 8, 45, 2));
        suppliers.put("SupplierB-P2", new Supplier(2, "SupplierB", "P2", 8, 15, 2));
        suppliers.put("SupplierC-P1", new Supplier(3, "SupplierC", "P1", 4, 55, 1));
        suppliers.put("SupplierC-P2", new Supplier(3, "SupplierC", "P2", 4, 18, 1));
    }

    public static Supplier getBestSupplier(String piece, int quantity, int daysUntilDueDate) {
        Supplier bestSupplier = null;
        System.out.println("Searching for best supplier for initial Piece: " + piece + ", Quantity: " + quantity + ", Days Until Due Date: " + daysUntilDueDate);
        for (Supplier supplier : suppliers.values()) {
            System.out.println("Evaluating Supplier: " + supplier.getName() + " for Piece: " + supplier.getPiece());
            if (supplier.getPiece().equals(piece) && quantity <= supplier.getMinimumOrder()) {
                System.out.println("Supplier " + supplier.getName() + " matches the piece and minimum order criteria.");
                if (daysUntilDueDate >= supplier.getDeliveryTime()) {
                    System.out.println("Supplier " + supplier.getName() + " can deliver within the due date.");
                    if (bestSupplier == null || supplier.getPricePerPiece() < bestSupplier.getPricePerPiece()) {
                        bestSupplier = supplier;
                        System.out.println("Supplier " + supplier.getName() + " is currently the best option with price per piece: " + supplier.getPricePerPiece());
                    }
                } else {
                    System.out.println("Supplier " + supplier.getName() + " cannot deliver within the due date.");
                }
            } else {
                System.out.println("Supplier " + supplier.getName() + " does not match the piece or minimum order criteria.");
            }
        }
        if (bestSupplier != null) {
            System.out.println("Best Supplier: " + bestSupplier.getName() + 
                               " Piece: " + bestSupplier.getPiece() + 
                               " Minimum Order: " + bestSupplier.getMinimumOrder() + 
                               " Price Per Piece: " + bestSupplier.getPricePerPiece() + 
                               " Delivery Time: " + bestSupplier.getDeliveryTime());
            System.out.println("Input Parameters - Initial Piece: " + piece + ", Quantity: " + quantity + ", Days Until Due Date: " + daysUntilDueDate);
        } else {
            System.out.println("No suitable supplier found for Initial Piece: " + piece + ", Quantity: " + quantity + ", Days Until Due Date: " + daysUntilDueDate);
        }
        return bestSupplier;
    }
}