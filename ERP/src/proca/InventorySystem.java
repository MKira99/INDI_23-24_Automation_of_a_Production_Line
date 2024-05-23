package proca;

import java.util.ArrayList;
import java.util.List;

public class InventorySystem {
    private List<Product> products;
    private List<Warehouse> warehouses;


    // Methods
    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    public void removeWarehouse(Warehouse warehouse) {
        warehouses.remove(warehouse);
    }

    public static int checkHas(Product product, int quantity){
        if(Warehouse.getQuantityForProduct(product.getProductId())>=quantity) {
            return 1;
        }
        else return 0;
    }

    /*public double getTotalInventoryValue() {
        double totalValue = 0;
        for (Warehouse warehouse : warehouses) {
            totalValue += warehouse.getTotalInventoryValue();
        }
        return totalValue;
    }*/
}
