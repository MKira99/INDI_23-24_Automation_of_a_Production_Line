package Others;


import java.util.ArrayList;
import java.util.List;

public class PurchaseSystem {
    private static List<Supplier> suppliers=new ArrayList<>();
    private static List<PurchaseOrder> purchaseOrders=new ArrayList<>();

    // Constructor
    public PurchaseSystem() {
        suppliers = new ArrayList<>();
        purchaseOrders = new ArrayList<>();
    }

    // Methods
    public static void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public void removeSupplier(Supplier supplier) {
        suppliers.remove(supplier);
    }

    public static void createPurchaseOrder(PurchaseOrder order) {
        purchaseOrders.add(order);
    }

    public void cancelPurchaseOrder(PurchaseOrder order) {
        purchaseOrders.remove(order);
    }

    public static List<PurchaseOrder> getPurchaseOrdersForSupplier(Supplier supplier) {
        List<PurchaseOrder> orders = new ArrayList<>();
        for (PurchaseOrder order : purchaseOrders) {
            if (order.getSupplier().equals(supplier)) {
                orders.add(order);
            }
        }
        return orders;
    }
}
