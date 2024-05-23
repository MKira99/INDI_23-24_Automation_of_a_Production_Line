package proca;

public class DataOrder {
    private static String workpiece;
    private static int quantity;
    private static int dueDate;

    public static void setOrderData(String workpiece, int quantity, int dueDate) {
        DataOrder.workpiece = workpiece;
        DataOrder.quantity = quantity;
        DataOrder.dueDate = dueDate;
    }

    public static String getWorkpiece() {
        return workpiece;
    }

    public static int getQuantity() {
        return quantity;
    }

    public static int getDueDate() {
        return dueDate;
    }

    public static void printOrderData() {
        System.out.println("Workpiece: " + workpiece);
        System.out.println("Quantity: " + quantity);
        System.out.println("Due Date: " + dueDate);
    }
}