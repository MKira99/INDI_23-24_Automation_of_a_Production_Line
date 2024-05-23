package proca;

import proca.Product;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    static ArrayList<Product> products=new ArrayList<Product>();

    public static void removeProduct (String productID, int quantity){
        Product product_remove = new Product(productID, quantity);
        products.remove(product_remove);
    }

    public static void addProduct(String productId, int quantity) {
        Product product = new Product(productId, quantity);
        products.add(product);
    }

    public static int getQuantityForProduct(String productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return product.getQuantity();
            }
        }
        return 0;
    }

}

