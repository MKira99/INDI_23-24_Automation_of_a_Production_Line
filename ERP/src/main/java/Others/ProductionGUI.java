package Others;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import com.bybutter.sisyphus.dsl.ordering.grammar.OrderListener;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import Others.DataOrder.*;

public class ProductionGUI extends JFrame implements OrderListener {

    private JTable mpsTable;
    private JTable purchasingTable;
    private JTable productionTable;
    private DefaultTableModel mpsModel;
    private DefaultTableModel purchasingModel;
    private DefaultTableModel productionModel;

    public ProductionGUI() {
        setTitle("Production and Purchasing Plans");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Master Production Schedule Table
        mpsModel = new DefaultTableModel();
        mpsModel.addColumn("Workpiece");
        mpsModel.addColumn("Quantity");
        mpsModel.addColumn("Due Date");
        mpsModel.addColumn("Final Date");
        mpsModel.addColumn("Total Cost");
        mpsModel.addColumn("Processing Time");

        mpsTable = new JTable(mpsModel);
        JScrollPane mpsScrollPane = new JScrollPane(mpsTable);
        mpsScrollPane.setBorder(BorderFactory.createTitledBorder("Master Production Schedule"));

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

        // Layout
        setLayout(new GridLayout(3, 1));
        add(mpsScrollPane);
        add(purchasingScrollPane);
        add(productionScrollPane);
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
    SwingUtilities.invokeLater(() -> {
        for (Order order : orders) {
            // Define the order data
            DataOrder.setOrderData(order.getWorkpiece(), order.getQuantity(), order.getDueDate());
            JSONObject summary = DataOrder.getOrderSummary();

            String workpiece = summary.getString("PieceType");
            int quantity = summary.getInt("Quantity");
            int dueDateDays = summary.getInt("DateStart");
            int processingTime = summary.getInt("ProcessingTime");
            double totalCost = summary.getDouble("TotalCost");
            int finalDateDays = summary.getInt("DateEnd");

            String dueDateStr = convertDaysToDate(dueDateDays);
            String finalDateStr = convertDaysToDate(finalDateDays);

            // Update Master Production Schedule
            mpsModel.addRow(new Object[]{
                workpiece, quantity, dueDateStr, finalDateStr, totalCost, processingTime / 60
            });

            // Update Purchasing Plan
            Supplier bestSupplier = DataOrder.getBestSupplier(); // Get the best supplier from DataOrder
            if (bestSupplier != null) {
                double cost = bestSupplier.getPricePerPiece() * bestSupplier.getMinimumOrder();
                String initialPiece = DataOrder.determineInitialPiece(workpiece); // Get the initial piece
                purchasingModel.addRow(new Object[]{
                    bestSupplier.getName(),
                    initialPiece, // Use the initial piece here
                    bestSupplier.getMinimumOrder(), // Use the minimum order quantity here
                    bestSupplier.getPricePerPiece(),
                    cost,
                    bestSupplier.getDeliveryTime()
                });
            }

            // Update Production Plan
            productionModel.addRow(new Object[]{workpiece, quantity, processingTime / 60});
        }
    });
}

    public static String convertDaysToDate(int days) {
        long millis = TimeUnit.DAYS.toMillis(days);
        java.util.Date date = new java.util.Date(millis);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd");
        return sdf.format(date);
    }
}