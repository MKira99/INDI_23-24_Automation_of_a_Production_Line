package Others;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import Others.DataOrder.*;

public class ProductionGUI extends JFrame implements OrderListener {

    private JTable mrpTable;
    private JTable purchasingTable;
    private JTable productionTable;
    private DefaultTableModel mrpModel;
    private DefaultTableModel purchasingModel;
    private DefaultTableModel productionModel;
    private JLabel currentDateLabel;

    public ProductionGUI() {
        setTitle("ERP System - MRP, Purchasing, and Production Plans");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Master Resource Planning (MRP) Table
        mrpModel = new DefaultTableModel();
        mrpModel.addColumn("Order ID");
        mrpModel.addColumn("Client Name");
        mrpModel.addColumn("Workpiece");
        mrpModel.addColumn("Quantity");
        mrpModel.addColumn("Due Date");
        mrpModel.addColumn("Final Date");
        mrpModel.addColumn("Total Cost");

        mrpTable = new JTable(mrpModel);
        JScrollPane mrpScrollPane = new JScrollPane(mrpTable);
        mrpScrollPane.setBorder(BorderFactory.createTitledBorder("Master Resource Planning (MRP)"));

        // Purchasing Plan Table
        purchasingModel = new DefaultTableModel();
        purchasingModel.addColumn("Supplier");
        purchasingModel.addColumn("Piece");
        purchasingModel.addColumn("Quantity");
        purchasingModel.addColumn("Price per Piece");
        purchasingModel.addColumn("Total Cost");
        purchasingModel.addColumn("Delivery Time");

        purchasingTable = new JTable(purchasingModel);
        JScrollPane purchasingScrollPane = new JScrollPane(purchasingTable);
        purchasingScrollPane.setBorder(BorderFactory.createTitledBorder("Purchasing Plan"));

        // Production Plan Table
        productionModel = new DefaultTableModel();
        productionModel.addColumn("Workpiece");
        productionModel.addColumn("Quantity");
        productionModel.addColumn("Processing Time");

        productionTable = new JTable(productionModel);
        JScrollPane productionScrollPane = new JScrollPane(productionTable);
        productionScrollPane.setBorder(BorderFactory.createTitledBorder("Production Plan"));

        // Current Date Label
        currentDateLabel = new JLabel("Current Date: " + getCurrentDate());
        currentDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentDateLabel.setFont(new Font("Serif", Font.BOLD, 16));
        currentDateLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Layout
        setLayout(new BorderLayout());
        add(currentDateLabel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.add(mrpScrollPane);
        centerPanel.add(purchasingScrollPane);
        centerPanel.add(productionScrollPane);
        add(centerPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductionGUI gui = new ProductionGUI();
            Threader.UDPServer.addOrderListener(gui);
            gui.setVisible(true);
        });
    }

    @Override
    public void onNewOrders(List<Order> orders) {

        ArrayList<OrderResult> ordersResult = (ArrayList<OrderResult>) OrderResult.getAllFinalOrders();
        Map<String, Boolean> visited = new HashMap<>();
        Map<String, Double> rawMaterialCosts = new HashMap<>();
        rawMaterialCosts.put("P1", 30.0);
        rawMaterialCosts.put("P2", 10.0);

        SwingUtilities.invokeLater(() -> {
            for (Order order : orders) {
                int finalDate=0;
                double totalCost=0;
                System.out.println("ordersResult" + ordersResult);
                int quantity = Integer.parseInt(order.quantity);
                int dueDate = Integer.parseInt(order.dueDate);
                double rawMaterialCost = DataOrder.calculateRawMaterialCost(order.workPiece, quantity, rawMaterialCosts, visited , new HashMap<>());
                int processingTime = (int) DataOrder.calculateProcessingTimeInDays(order.workPiece, quantity, true) * 60; // in minutes
                //int startDate = dueDate - (processingTime / 60); // Assuming processingTime is in hours
                
                // Define the order data
                String workpiece = order.workPiece;
                //double totalCost = rawMaterialCost + processingTime * 1; // Example calculation, adjust as necessary
                for(OrderResult orderResult : ordersResult){
                    if(order.orderId == orderResult.orderId){
                        finalDate=orderResult.getEndDate();
                        totalCost=orderResult.getCost();
                    }
                }
                String dueDateStr = String.format("%02d", dueDate);
                String finalDateStr = String.format("%02d", finalDate); // Assuming endDate is in days

                // Update Master Resource Planning (MRP)
                mrpModel.addRow(new Object[]{
                    order.orderId, order.clientName, workpiece, quantity, dueDateStr, finalDateStr, totalCost
                });

                // Update Purchasing Plan
                Supplier bestSupplier = DataOrder.getBestSupplier(workpiece, quantity); // Get the best supplier from DataOrder
                if (bestSupplier != null) {
                    System.out.println("Entrei caralhooooo");
                    double cost = bestSupplier.pricePerPiece * quantity;
                    purchasingModel.addRow(new Object[]{
                        bestSupplier.name,
                        bestSupplier.piece,
                        quantity,
                        bestSupplier.pricePerPiece,
                        cost,
                        bestSupplier.deliveryTime
                    });
                }
                else {
                    System.out.println("Não entrei fdsssss");
                }

                // Update Production Plan
                productionModel.addRow(new Object[]{workpiece, quantity, processingTime / 60});
            }
        });
    }

    public static String convertDaysToDate(int days) {
        LocalDate epoch = LocalDate.ofEpochDay(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return epoch.format(formatter);
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new Date());
    }
}