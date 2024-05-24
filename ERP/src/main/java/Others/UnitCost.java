package Others;

import java.util.HashMap;
import java.util.Map;

public class UnitCost {

    // Static map to store processing times for different pieces
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

    public static double calculateTotalCost(double rawMaterialCost, int arrivalDate, int dispatchDate, String startingPiece, String producedPiece) {
        int processingTime = getProcessingTime(startingPiece, producedPiece);
        double productionCost = calculateProductionCost(processingTime);
        double depreciationCost = calculateDepreciationCost(rawMaterialCost, arrivalDate, dispatchDate);

        return rawMaterialCost + productionCost + depreciationCost;
    }

    private static int getProcessingTime(String startingPiece, String producedPiece) {
        String key = startingPiece + "-" + producedPiece;
        return processingTimes.getOrDefault(key, 0);
    }

    private static double calculateProductionCost(int processingTime) {
        // Production cost is €1 per second
        return processingTime * 1.0;
    }

    private static double calculateDepreciationCost(double rawMaterialCost, int arrivalDate, int dispatchDate) {
        int duration = dispatchDate - arrivalDate;
        return rawMaterialCost * duration * 0.01;
    }
}