package Others;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import java.awt.*;
import java.util.List;
import Others.ConsolidatedOrderSystem.*;
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
                DataOrder.setOrderData(order.getWorkpiece(), order.getQuantity(), order.getDueDate());
                JSONObject summary = DataOrder.getOrderSummary();
                String workpiece = summary.get("Type").toString();
                String quantity = summary.get("Quantity").toString();
                String dueDate = summary.get("StartDate").toString();
                String finalDate = summary.get("EndDate").toString();
                String totalCost = summary.get("TotalCost").toString();
                String processingTime = summary.get("ProcessingTime").toString();
                mpsModel.addRow(new Object[]{workpiece, quantity, dueDate, finalDate, totalCost, processingTime});

                // Update Purchasing Plan
                Supplier bestSupplier = Supplier.getBestSupplier(workpiece, Integer.parseInt(quantity));
                if (bestSupplier != null) {
                    int qty = bestSupplier.getMinimumOrder();
                    double cost = bestSupplier.getPricePerPiece() * qty;
                    purchasingModel.addRow(new Object[]{
                        bestSupplier.getName(),
                        bestSupplier.getPiece(),
                        qty,
                        bestSupplier.getPricePerPiece(),
                        cost,
                        bestSupplier.getDeliveryTime()
                    });
                }

                // Update Production Plan
                int procTime = DataOrder.getProcessingTime(workpiece);
                productionModel.addRow(new Object[]{workpiece, quantity, procTime});
            }
        });
    }
}