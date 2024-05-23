package proca;

import java.util.*;
import java.io.*;
import java.net.*;

public class ProductionPlans {
    // Define the supplier and product information
    static String[][] supplierDetails = {
            {"SupplierA", "P1", "16", "30", "4 days"},   // SupplierA supplies P1 order of 16 units, cost 30, delivery in 4 days
            {"SupplierA", "P2", "16", "10", "4 days"},   // SupplierA supplies P2 order of 16 units, cost 10, delivery in 4 days
            {"SupplierB", "P1", "8", "45", "2 days"},    // SupplierB supplies P1 order of 8 units, cost 45, delivery in 2 days
            {"SupplierB", "P2", "8", "15", "2 days"},    // SupplierB supplies P2 order of 8 units, cost 15, delivery in 2 days
            {"SupplierC", "P1", "4", "55", "1 day"},     // SupplierC supplies P1 order of 4 units, cost 55, delivery in 1 day
            {"SupplierC", "P2", "4", "18", "1 day"}      // SupplierC supplies P2 order of 4 units, cost 18, delivery in 1 day
    };

    // Define the production hierarchy information
    static String[][] productionHierarchy = {
            {"P1", "P3", "T1", "45"},   // P1 is transformed to P3 with process T1 taking 45 s
            {"P3", "P4", "T2", "15"},   // P3 is transformed to P4 with process T2 taking 15 s
            {"P3", "P4", "T3", "25"},   // P3 is transformed to P4 with process T3 taking 25 s
            {"P4", "P5", "T4", "25"},   // P4 is transformed to P5 with process T4 taking 25 s
            {"P4", "P6", "T2", "25"},   // P4 is transformed to P6 with process T2 taking 25 s
            {"P4", "P7", "T3", "15"},   // P4 is transformed to P7 with process T3 taking 15 s
            {"P2", "P8", "T1", "45"},   // P2 is transformed to P8 with process T1 taking 45 s
            {"P8", "P7", "T6", "15"},   // P8 is transformed to P7 with process T6 taking 15 s
            {"P8", "P9", "T5", "45"}    // P8 is transformed to P9 with process T5 taking 45 s
    };

    // Method to find the optimal supplier based on cost, penalties, and deadlines
    public static String[] findOptimalSupplier(String componentType, int neededQuantity, int deadline, double lateFee, double earlyFee) {
        String[] optimalSupplier = null;
        double minimumCost = Double.MAX_VALUE;   // Initialize with maximum value for comparison
        int orderDay = 0;
        int shippingDuration = 0;

        // Loop through each supplier to evaluate
        for (String[] supplier : supplierDetails) {
            if (supplier[1].equals(componentType)) {   // Check if supplier provides the required component type
                int minimumOrderQuantity = Integer.parseInt(supplier[2]);
                int deliveryDuration = Integer.parseInt(supplier[4].split(" ")[0]);
                double unitCost = Double.parseDouble(supplier[3]);

                int orderQuantity = Math.max(neededQuantity, minimumOrderQuantity);  // Ensure minimum order quantity is met
                double totalCost = orderQuantity * unitCost;

                double penalty = 0;
                if (deliveryDuration > deadline) {   // Calculate late penalty if delivery is after the deadline
                    penalty = (deliveryDuration - deadline) * lateFee;
                    orderDay = 0; // Set order day to current day
                } else if (deliveryDuration < deadline) {   // Calculate early penalty if delivery is before the deadline
                    penalty = (deadline - deliveryDuration) * earlyFee;
                    orderDay = deadline - deliveryDuration; // Set order day x days after receiving the order
                }

                double finalCost = totalCost + penalty;   // Calculate the final cost including penalties

                if (finalCost < minimumCost) {   // Update optimal supplier if a lower cost is found
                    minimumCost = finalCost;
                    shippingDuration = deliveryDuration;
                    optimalSupplier = supplier;
                }
            }
        }

        // Handle case where no supplier is found for the component type
        if (optimalSupplier == null) {
            System.out.println("No supplier found for component type: " + componentType);
            optimalSupplier = componentType.equals("P1") ? supplierDetails[4] : supplierDetails[5];
        }

        // Add order day to the end of the optimalSupplier array
        String[] optimalSupplierWithOrderDay = Arrays.copyOf(optimalSupplier, optimalSupplier.length + 2);
        optimalSupplierWithOrderDay[optimalSupplierWithOrderDay.length - 2] = String.valueOf(orderDay);
        optimalSupplierWithOrderDay[optimalSupplierWithOrderDay.length - 1] = String.valueOf(orderDay + shippingDuration);

        return optimalSupplierWithOrderDay;
    }

    // Method to get all possible production paths for a final component
    public static List<String[]> getProductionPaths(String finalComponent) {
        List<String[]> productionPaths = new ArrayList<>();
        Map<String, List<String[]>> memoization = new HashMap<>();   // Memoization to store already explored paths
        explorePaths("P1", finalComponent, "", 0, productionPaths, memoization);
        explorePaths("P2", finalComponent, "", 0, productionPaths, memoization);
        return productionPaths;
    }

    // Method to find the quickest production path based on time
    public static String findQuickestPath(List<String[]> productionPaths) {
        return productionPaths.stream()
                .min(Comparator.comparingInt(path -> Integer.parseInt(path[1])))
                .map(path -> path[0])
                .orElse("");
    }

    // Method to extract the initial component from a production path
    public static String extractInitialComponent(String path) {
        return path.split(" ")[0];
    }

    // Helper method to explore all possible paths recursively using DFS
    private static void explorePaths(String currentComponent, String finalComponent, String pathSoFar, int elapsedTime, List<String[]> productionPaths, Map<String, List<String[]>> memoization) {
        pathSoFar += currentComponent + " ";
        if (currentComponent.equals(finalComponent)) {
            productionPaths.add(new String[]{pathSoFar.trim(), String.valueOf(elapsedTime)});
            return;
        }

        if (memoization.containsKey(currentComponent)) {   // Use memoized results if available
            for (String[] info : memoization.get(currentComponent)) {
                explorePaths(info[1], finalComponent, pathSoFar, elapsedTime + Integer.parseInt(info[3]), productionPaths, memoization);
            }
        } else {
            List<String[]> paths = new ArrayList<>();
            for (String[] info : productionHierarchy) {
                if (info[0].equals(currentComponent)) {
                    paths.add(info);
                    explorePaths(info[1], finalComponent, pathSoFar, elapsedTime + Integer.parseInt(info[3]), productionPaths, memoization);
                }
            }
            memoization.put(currentComponent, paths);   // Memoize the results
        }
    }

    // Method to compute the production schedule given a production path
    public static Map<Integer, String> computeProductionSchedule(String path, int quantity, int startDay) {
        final int conveyorDuration = 5;   // Time taken for conveyor transport
        final int returnDuration = 10;    // Time taken for return transport
        String[] componentTypes = path.split(" ");
        int accumulatedTime = 0;
        Map<Integer, String> productionSchedule = new HashMap<>();

        for (int i = 0; i < componentTypes.length - 1; i++) {
            for (String[] info : productionHierarchy) {
                if (info[0].equals(componentTypes[i]) && info[1].equals(componentTypes[i + 1])) {
                    int transformationDuration = Integer.parseInt(info[3]);
                    int productionDuration = transformationDuration + conveyorDuration;
                    if (i != 0 && i != componentTypes.length - 2) {
                        productionDuration += returnDuration;
                    }
                    accumulatedTime += productionDuration;
                    int day = accumulatedTime / 60 + startDay;
                    String expectedComponent = componentTypes[i + 1] + " " + quantity;
                    productionSchedule.put(day, expectedComponent);
                    break;
                }
            }
        }
        return productionSchedule;
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Create an order using ConsolidatedOrderSystem
        ConsolidatedOrderSystem.Order order = new ConsolidatedOrderSystem.Order("ClientA", 1, "P1", 10, 5, 2, 1);
        ConsolidatedOrderSystem.OrderSystem.addOrder(order);

        // Get the order by number
        ConsolidatedOrderSystem.Order retrievedOrder = ConsolidatedOrderSystem.OrderSystem.getOrderByNumber(1);
        System.out.println("Order 1: " + retrievedOrder);

        // Plan production for the order
        if (retrievedOrder != null) {
            String[] optimalSupplier = findOptimalSupplier(retrievedOrder.getWorkpiece(), retrievedOrder.getQuantity(), retrievedOrder.getDueDate(), retrievedOrder.getLatePen(), retrievedOrder.getEarlyPen());
            System.out.println("Optimal Supplier: " + Arrays.toString(optimalSupplier));

            List<String[]> paths = getProductionPaths(retrievedOrder.getWorkpiece());
            String quickestPath = findQuickestPath(paths);
            System.out.println("Quickest Path: " + quickestPath);

            Map<Integer, String> schedule = computeProductionSchedule(quickest Path, retrievedOrder.getQuantity(), 0);
            System.out.println("Production Schedule: " + schedule);
        }
    }
}