package Others;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import Others.ConsolidatedOrderSystem.Order;

public class DataOrder {
    private static String workpiece;
    private static int quantity;
    private static int dueDate;
    private static double rawMaterialCost;
    private static int arrivalDate;
    private static final Map<String, Integer> processingTimes = new HashMap<>();

    static {
        // Initialize processing times based on the provided table
        processingTimes.put("P1-P3", 45);
        processingTimes.put("P3-P4", 15);
        processingTimes.put("P4-P5", 25);
        processingTimes.put("P4-P6", 25);
        processingTimes.put("P4-P7", 15);
        processingTimes.put("P2-P8", 45);
        processingTimes.put("P8-P7", 15);
        processingTimes.put("P8-P9", 45);
    }

    public static void setOrderData(String workpiece, int quantity, int dueDate) {
        DataOrder.workpiece = workpiece;
        DataOrder.quantity = quantity;
        DataOrder.dueDate = dueDate;

        Supplier bestSupplier = Supplier.getBestSupplier(workpiece, quantity);
        if (bestSupplier != null) {
            DataOrder.rawMaterialCost = bestSupplier.getPricePerPiece() * quantity;
            DataOrder.arrivalDate = dueDate - bestSupplier.getDeliveryTime();
        } else {
            DataOrder.rawMaterialCost = 0;
            DataOrder.arrivalDate = dueDate;
        }
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

    public static double getRawMaterialCost() {
        return rawMaterialCost;
    }

    public static int getArrivalDate() {
        return arrivalDate;
    }

    public static int getProcessingTime(String producedPiece) {
        String key = workpiece + "-" + producedPiece;
        return processingTimes.getOrDefault(key, 0);
    }

    public static double calculateTotalCost() {
        int processingTime = getProcessingTime(workpiece); // Assuming workpiece to producedPiece mapping
        double productionCost = calculateProductionCost(processingTime);
        double depreciationCost = calculateDepreciationCost(rawMaterialCost, arrivalDate, dueDate);

        return rawMaterialCost + productionCost + depreciationCost;
    }

    private static double calculateProductionCost(int processingTime) {
        // Production cost is €1 per second
        return processingTime * 1.0;
    }

    private static double calculateDepreciationCost(double rawMaterialCost, int arrivalDate, int dispatchDate) {
        int duration = dispatchDate - arrivalDate;
        return rawMaterialCost * duration * 0.01;
    }

    public static void printOrderData() {
        System.out.println("Workpiece: " + workpiece);
        System.out.println("Quantity: " + quantity);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Raw Material Cost: " + rawMaterialCost);
        System.out.println("Arrival Date: " + arrivalDate);
    }

    public static JSONObject getOrderSummary() {
        int processingTime = getProcessingTime(workpiece); // Assuming workpiece to producedPiece mapping
        double totalCost = calculateTotalCost();
        int finalDate = arrivalDate + processingTime; // Simplificação: considerando que o processamento começa no dia de chegada
        JSONObject orderSummary = new JSONObject();
        orderSummary.put("Type", workpiece);
        orderSummary.put("Quantity", quantity);
        orderSummary.put("StartDate", dueDate);
        orderSummary.put("EndDate", finalDate);
        orderSummary.put("TotalCost", totalCost);
        orderSummary.put("ProcessingTime", processingTime);
        return orderSummary;
    }

    public interface OrderListener {
    void onNewOrders(List<Order> orders);
    }
}