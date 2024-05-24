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

    // Construtor completo
    public Supplier(int supplierId, String name, String piece, int minimumOrder, double pricePerPiece, int deliveryTime) {
        this.supplierId = supplierId;
        this.name = name;
        this.piece = piece;
        this.minimumOrder = minimumOrder;
        this.pricePerPiece = pricePerPiece;
        this.deliveryTime = deliveryTime;
    }

    // Construtor para o array de preços
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
        suppliers.put("SupplierA-P1", new Supplier(1, "SupplierA", "P1", 16, 30.0, 4));
        suppliers.put("SupplierA-P2", new Supplier(1, "SupplierA", "P2", 16, 10.0, 4));
        suppliers.put("SupplierB-P1", new Supplier(2, "SupplierB", "P1", 8, 45.0, 2));
        suppliers.put("SupplierB-P2", new Supplier(2, "SupplierB", "P2", 8, 15.0, 2));
        suppliers.put("SupplierC-P1", new Supplier(3, "SupplierC", "P1", 4, 55.0, 1));
        suppliers.put("SupplierC-P2", new Supplier(3, "SupplierC", "P2", 4, 18.0, 1));
    }

    public static Supplier getBestSupplier(String piece, int quantity) {
        Supplier bestSupplier = null;
        for (Supplier supplier : suppliers.values()) {
            if (supplier.getPiece().equals(piece) && supplier.getMinimumOrder() <= quantity) {
                if (bestSupplier == null || supplier.getPricePerPiece() < bestSupplier.getPricePerPiece()) {
                    bestSupplier = supplier;
                }
            }
        }
        return bestSupplier;
    }
}