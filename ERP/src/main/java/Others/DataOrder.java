package Others;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import Others.ConsolidatedOrderSystem.Order;
public class DataOrder {
    private static String workpiece;
    private static int quantity;
    private static int dueDate; // in days
    private static double rawMaterialCost;
    private static int arrivalDate; // in seconds
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
            DataOrder.arrivalDate = dueDate * 86400 - bestSupplier.getDeliveryTime() * 86400; // Converting days to seconds
        } else {
            DataOrder.rawMaterialCost = 0;
            DataOrder.arrivalDate = dueDate * 86400; // Converting days to seconds
        }
    }

    public static void saveOrderToJson(Order order, String clientID) {
        JSONObject json = new JSONObject();
        json.put("ClientID", clientID);
        json.put("ClientName", order.getClientName());
        json.put("OrderNumber", order.getOrderNumber());
        json.put("Workpiece", order.getWorkpiece());
        json.put("Quantity", order.getQuantity());
        json.put("DueDate", order.getDueDate());
        json.put("FinalDate", DataOrder.getFinalDateInDays());

        try (FileWriter file = new FileWriter("order_" + clientID + ".json")) {
            file.write(json.toString(4)); // Indent with 4 spaces for readability
            System.out.println("Successfully saved order to JSON file: order_" + clientID + ".json");
        } catch (IOException e) {
            e.printStackTrace();
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

    public static int getFinalDateInDays() {
        int totalProcessingTime = getTotalProcessingTime(); // Total processing time in seconds
        double processingTimeInDays = (double) totalProcessingTime / 60; // Convert processing time from seconds to days
        return (int) Math.ceil(dueDate + processingTimeInDays); // Adding processing time in days to due date and rounding up
    }

    public static int getProcessingTime(String startPiece, String endPiece) {
        String key = startPiece + "-" + endPiece;
        return processingTimes.getOrDefault(key, 0);
    }

    public static int getTotalProcessingTime() {
        int totalTime = 0;
        String currentPiece = getInitialPiece(); // Starting piece

        while (!currentPiece.equals(workpiece)) {
            String nextPiece = getNextPiece(currentPiece);
            if (nextPiece == null) {
                break;
            }
            totalTime += getProcessingTime(currentPiece, nextPiece);
            currentPiece = nextPiece;
        }
        return totalTime * quantity; // Total processing time in seconds
    }

    private static String getInitialPiece() {
        // Here you define the logic to find the initial piece
        // Based on the table, we can assume the initial piece is "P1" or "P2" depending on the workpiece needed
        if (workpiece.startsWith("P1") || workpiece.startsWith("P3") || workpiece.startsWith("P4") || workpiece.startsWith("P5") || workpiece.startsWith("P6") || workpiece.startsWith("P7")) {
            return "P1";
        } else if (workpiece.startsWith("P8") || workpiece.startsWith("P9")) {
            return "P2";
        }
        return "P1"; // Default to P1 if no other conditions are met
    }

    private static String getNextPiece(String currentPiece) {
        for (String key : processingTimes.keySet()) {
            if (key.startsWith(currentPiece + "-")) {
                return key.split("-")[1];
            }
        }
        return null;
    }

    public static double calculateTotalCost() {
        int totalProcessingTime = getTotalProcessingTime(); // Total processing time in seconds
        double productionCost = calculateProductionCost(totalProcessingTime);
        double depreciationCost = calculateDepreciationCost(rawMaterialCost, arrivalDate, dueDate * 86400);

        return rawMaterialCost + productionCost + depreciationCost;
    }

    private static double calculateProductionCost(int processingTime) {
        // Production cost is €1 per second
        return processingTime * 1.0;
    }

    private static double calculateDepreciationCost(double rawMaterialCost, int arrivalDate, int dispatchDate) {
        int duration = dispatchDate - arrivalDate;
        return rawMaterialCost * (duration / 86400.0) * 0.01; // Converting seconds to days
    }

    public static void printOrderData() {
        System.out.println("Workpiece: " + workpiece);
        System.out.println("Quantity: " + quantity);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Raw Material Cost: " + rawMaterialCost);
        System.out.println("Arrival Date: " + arrivalDate / 86400 + " days"); // Converting seconds to days
    }

    public static JSONObject getOrderSummary() {
        int totalProcessingTime = getTotalProcessingTime(); // Total processing time in seconds
        double totalCost = calculateTotalCost();
        int finalDate = getFinalDateInDays(); // Calculating final date
        JSONObject orderSummary = new JSONObject();
        orderSummary.put("Type", workpiece);
        orderSummary.put("Quantity", quantity);
        orderSummary.put("StartDate", dueDate);
        orderSummary.put("EndDate", finalDate);
        orderSummary.put("TotalCost", totalCost);
        orderSummary.put("ProcessingTime", totalProcessingTime);
        return orderSummary;
    }

    public static String generateClientID(Order order) {
        return order.getClientName() + "_" + order.getOrderNumber();
    }

    public interface OrderListener {
        void onNewOrders(List<Order> orders);
    }
}