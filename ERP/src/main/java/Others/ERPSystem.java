package Others;


public class ERPSystem {
    private static InventorySystem inventorySystem;
    private static PurchaseSystem purchaseSystem;

    // Constructor
    public ERPSystem() {
        inventorySystem = new InventorySystem();
        purchaseSystem = new PurchaseSystem();
    }

    // Methods
    //public double getTotalInventoryValue() {
      //  return inventorySystem.getTotalInventoryValue();
    //}

    public void addProduct(Product product) {
        inventorySystem.addProduct(product);
    }

    public void removeProduct(Product product) {
        inventorySystem.removeProduct(product);
    }

    public void addWarehouse(Warehouse warehouse) {
        inventorySystem.addWarehouse(warehouse);
    }

    public void removeWarehouse(Warehouse warehouse) {
        inventorySystem.removeWarehouse(warehouse);
    }

    public static void addSupplier(Supplier supplier) {
        purchaseSystem.addSupplier(supplier);
    }

    public void removeSupplier(Supplier supplier) {
        purchaseSystem.removeSupplier(supplier);
    }

    public void createPurchaseOrder(PurchaseOrder order) {
        purchaseSystem.createPurchaseOrder(order);
    }
}
