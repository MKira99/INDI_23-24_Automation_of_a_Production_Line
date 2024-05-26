package Others;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Others.ConsolidatedOrderSystem.Order;

public class DataOrder {
    private static String workpiece;
    private static int quantity;
    private static int dueDate; // in days
    private static int finalDate; // in days
    private static double rawMaterialCost;
    private static double totalCost;
    private static int arrivalDate; // in seconds
    private static Supplier bestSupplier; // Add this field to store the best supplier
    private static final Map<String, Integer> processingTimes = new HashMap<>();
    private static final Map<String, String> pieceMapping = new HashMap<>();

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

        // Initialize piece mappings based on the provided table
        pieceMapping.put("P3", "P1");
        pieceMapping.put("P4", "P3");
        pieceMapping.put("P5", "P4");
        pieceMapping.put("P6", "P4");
        pieceMapping.put("P7", "P4");
        pieceMapping.put("P8", "P2");
        pieceMapping.put("P9", "P8");
    }

    public static void setOrderData(String workpiece, int quantity, int dueDate) {
        DataOrder.workpiece = workpiece;
        DataOrder.quantity = quantity;
        DataOrder.dueDate = dueDate;

        int daysUntilDueDate = dueDate; // Assuming dueDate is in days
        bestSupplier = Supplier.getBestSupplier(determineInitialPiece(workpiece), quantity, daysUntilDueDate);
        if (bestSupplier != null) {
            DataOrder.rawMaterialCost = bestSupplier.getPricePerPiece() * quantity;
            DataOrder.arrivalDate = dueDate * 60 - bestSupplier.getDeliveryTime() * 60; // Converting days to seconds
        } else {
            DataOrder.rawMaterialCost = 0;
            DataOrder.arrivalDate = dueDate * 60; // Converting days to seconds
        }
    }

    public static Supplier getBestSupplier() {
        return bestSupplier;
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

    public static int calculateProcessingTimeForOrder(Order order) {
        int totalTime = 0;
        String currentPiece = determineInitialPiece(order.getWorkpiece());
        String finalPiece = order.getWorkpiece();
        int quantity = order.getQuantity();
    
        while (!currentPiece.equals(finalPiece)) {
            String nextPiece = getNextPiece(currentPiece);
            if (nextPiece == null) {
                break;
            }
            totalTime += getProcessingTime(currentPiece, nextPiece);
            currentPiece = nextPiece;
        }
        return totalTime * quantity;
    }


    public static int getQuantity() {
        return quantity;
    }

    public static int getDueDate() {
        return dueDate;
    }

    public static int getFinalDate() {
        return finalDate;
    }

    public static double getRawMaterialCost() {
        return rawMaterialCost;
    }

    public static double getTotalCost() {
        return totalCost;
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
        return determineInitialPiece(workpiece);
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
        double depreciationCost = calculateDepreciationCost(rawMaterialCost, arrivalDate, dueDate * 60);

        return rawMaterialCost + productionCost + depreciationCost;
    }

    private static double calculateProductionCost(int processingTime) {
        // Production cost is €1 per second
        return processingTime * 1.0;
    }

    private static double calculateDepreciationCost(double rawMaterialCost, int arrivalDate, int dispatchDate) {
        int duration = dispatchDate - arrivalDate;
        return rawMaterialCost * (duration / 60) * 0.01; // Converting seconds to days
    }

    public static void printOrderData() {
        System.out.println("Workpiece: " + workpiece);
        System.out.println("Quantity: " + quantity);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Raw Material Cost: " + rawMaterialCost);
        System.out.println("Arrival Date: " + arrivalDate / 60 + " days"); // Converting seconds to days
    }

    public static JSONObject getOrderSummary() {
        int totalProcessingTime = getTotalProcessingTime(); // Total processing time in seconds
        totalCost = calculateTotalCost();
        finalDate = getFinalDateInDays(); // Calculating final date
        JSONObject orderSummary = new JSONObject();

        orderSummary.put("OrderID", "ORDERIDPLEASE");
        orderSummary.put("PieceType", workpiece);
        orderSummary.put("Quantity", quantity);
        orderSummary.put("DateStart", dueDate);
        orderSummary.put("DateEnd", finalDate);
        orderSummary.put("TotalCost", totalCost);
        orderSummary.put("ProcessingTime", totalProcessingTime);
        return orderSummary;
    }

    public static String generateClientID(Order order) {
        return order.getClientName() + "_" + order.getOrderNumber();
    }

    public static void setDueDate(int newDueDate) {
        DataOrder.dueDate = newDueDate;
    }

    public static String determineInitialPiece(String finalPiece) {
        String currentPiece = finalPiece;
        while (pieceMapping.containsKey(currentPiece)) {
            currentPiece = pieceMapping.get(currentPiece);
        }
        return currentPiece;
    }

    public interface OrderListener {
        void onNewOrders(List<Order> orders);
    }
}
