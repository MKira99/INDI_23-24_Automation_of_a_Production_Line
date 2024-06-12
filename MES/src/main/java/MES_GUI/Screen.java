package MES_GUI;

import MES_Logic.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;


public class Screen extends JFrame {

    private static OrderList HistoryOrders = new OrderList();
    private static OrderList showOrders = new OrderList();
    private static List<Order> ActiveOrder = new ArrayList<>();
    private static List<Order> OldOrder = new ArrayList<>();
    JLabel BackgroundJLabel;

    public Screen() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the image
                ImageIcon imageIcon = new ImageIcon("src\\main\\java\\MES_GUI\\images\\background.jpg");
                Image image = imageIcon.getImage();
                 // Create a rendering hint to make the image smoother
                 ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                // Draw the image at the specified location
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Create the frame
        JFrame frame = new JFrame("MES User Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        // Create a panel with vertical layout for the title and buttons
        //JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Create and set the title label
        JLabel titleLabel = new JLabel("MES User Interface");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));  // Increased font size
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        
        // Create the buttons
        JButton button1 = new JButton("Current Orders");
        button1.setFont(new Font("Arial", Font.PLAIN, 24));  // Increased font size
        button1.setMaximumSize(new Dimension(300, 80));  // Set a larger size
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setBackground(new java.awt.Color(0, 152, 255));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when button 1 is clicked
                showOrdersFrame();
            }
        });
        
        JButton button2 = new JButton("Orders Status");
        button2.setFont(new Font("Arial", Font.PLAIN, 24));  // Increased font size
        button2.setMaximumSize(new Dimension(300, 80));  // Set a larger size
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.setForeground(new java.awt.Color(255, 255, 255));
        button2.setBackground(new java.awt.Color(0, 152, 255));
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when button 2 is clicked
                showOrdersStatusFrame();
            }
        });
        
        /*JButton button3 = new JButton("Machines Statistics");
        button3.setFont(new Font("Arial", Font.PLAIN, 24));  // Increased font size
        button3.setMaximumSize(new Dimension(300, 80));  // Set a larger size
        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
        button3.setForeground(new java.awt.Color(255, 255, 255));
        button3.setBackground(new java.awt.Color(0, 152, 255));
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when button 3 is clicked
                machinesStatistics();
            }
        });
        
        JButton button4 = new JButton("Unloaded Work-pieces");
        button4.setFont(new Font("Arial", Font.PLAIN, 24));  // Increased font size
        button4.setMaximumSize(new Dimension(300, 80));  // Set a larger size
        button4.setAlignmentX(Component.CENTER_ALIGNMENT);
        button4.setForeground(new java.awt.Color(255, 255, 255));
        button4.setBackground(new java.awt.Color(0, 152, 255));
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when button 4 is clicked
                unloadedWorkPieces();
            }
        });*/
        
        // Add spacing around the buttons
        panel.add(Box.createRigidArea(new Dimension(0, 40))); // space above title
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 80))); // space between title and button 1
        panel.add(button1);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between buttons
        panel.add(button2);
        /*panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between buttons
        panel.add(button3);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between buttons
        panel.add(button4);*/
        
        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);
        
        // Make the frame visible
        frame.setVisible(true);
    
    }
    
    // Method to show the frame for "Current Orders"
    private void showOrdersFrame() {
        JFrame ordersFrame = new JFrame("Current Orders");
        ordersFrame.setSize(600, 400);
        ordersFrame.setLocationRelativeTo(null); // Center the new frame
        
        
        // Define the data with time required
        showOrders.sortlist();
        ActiveOrder = showOrders.getListOrder();
        
        // Convert the list to a 2D array
        Object[][] data = new Object[ActiveOrder.size()][5];
        for (int i = 0; i < ActiveOrder.size(); i++) {
            data[i][0] = ActiveOrder.get(i).getOrderID();
            data[i][1] = ActiveOrder.get(i).getPieceType();
            data[i][2] = ActiveOrder.get(i).getQuantity();
            data[i][3] = ActiveOrder.get(i).getStartDay();
            data[i][4] = ActiveOrder.get(i).getEndDay();
        }
        
        // Column names
        String[] columnNames = {"OrderID", "PieceNr", "Quantity","StartDay","EndDay"};
            
        // Create a custom table model with column titles
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        // Create a JTable with the model
        JTable table = new JTable(model);
        
        // Center the text in the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add scroll pane to the frame
        ordersFrame.add(scrollPane);
        
        // Make the frame visible
        ordersFrame.setVisible(true);
    }
    
    // Method to show the frame for "Orders Status"
    private void showOrdersStatusFrame() {
        JFrame statusFrame = new JFrame("Orders Status");
        statusFrame.setSize(600, 400);
        statusFrame.setLocationRelativeTo(null); // Center the new frame
        // Define the data with time required
        HistoryOrders.sortlist();
        OldOrder = HistoryOrders.getListOrder();
        
        // Convert the list to a 2D array
        Object[][] data = new Object[OldOrder.size()][5];
        for (int i = 0; i < OldOrder.size(); i++) {
            data[i][0] = OldOrder.get(i).getOrderID();
            data[i][1] = OldOrder.get(i).getPieceType();
            data[i][2] = OldOrder.get(i).getQuantity();
            data[i][3] = OldOrder.get(i).getStartDay();
            data[i][4] = OldOrder.get(i).getEndDay();
        }
        
        // Column names
        String[] columnNames = {"OrderID", "PieceNr", "Quantity","StartDay","EndDay"};
            
        // Create a custom table model with column titles
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        // Create a JTable with the model
        JTable table = new JTable(model);
        
        // Center the text in the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add scroll pane to the frame
        statusFrame.add(scrollPane);
        
        // Make the frame visible
        statusFrame.setVisible(true);
        
    }

    // Method to create a new frame
    /*private static void machinesStatistics() {
        JFrame statusFrame = new JFrame("Machine Statistics");
        statusFrame.setSize(400, 300);
        statusFrame.setLocationRelativeTo(null); // Center the new frame
        statusFrame.setVisible(true);
        // Define the data with time required
        List<Object[]> orders = new ArrayList<>();
        orders.add(new Object[]{"1", "Product A", "10", 2.5});
        orders.add(new Object[]{"2", "Product B", "5", 1.2});
        orders.add(new Object[]{"3", "Product C", "8", 3.1});
        orders.add(new Object[]{"4", "Product D", "15", 0.5});
        
        // Sort the data based on the time required
        orders.sort(Comparator.comparingDouble(o -> (Double) o[3]));

        
        // Create a table model with column titles
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Number");
        model.addColumn("Total Operating Time");
        model.addColumn("Total");
        model.addColumn("Time To Do ");

        // Add the sorted data to the table model
        for (Object[] order : orders) {
            model.addRow(order);
        }

        // Create a JTable with the model
        JTable table1 = new JTable(model);
        
        // Center the text in the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table1);
        
        // Add scroll pane to the frame
        statusFrame.add(scrollPane);
        
        // Make the frame visible
        statusFrame.setVisible(true);
    }
    
    // Method to create a new frame
    private static void unloadedWorkPieces() {
        JFrame statusFrame = new JFrame("Unloaded Work-Pieces");
        statusFrame.setSize(400, 300);
        statusFrame.setLocationRelativeTo(null); // Center the new frame
        statusFrame.setVisible(true);
        // Define the data with time required
        List<Object[]> orders = new ArrayList<>();
        orders.add(new Object[]{"1", "Product A", "10", 2.5});
        orders.add(new Object[]{"2", "Product B", "5", 1.2});
        orders.add(new Object[]{"3", "Product C", "8", 3.1});
        orders.add(new Object[]{"4", "Product D", "15", 0.5});
        
        // Sort the data based on the time required
        orders.sort(Comparator.comparingDouble(o -> (Double) o[3]));

        
        // Create a table model with column titles
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Number");
        model.addColumn("Total Operating Time");
        model.addColumn("Total");
        model.addColumn("Time To Do ");

        // Add the sorted data to the table model
        for (Object[] order : orders) {
            model.addRow(order);
        }

        // Create a JTable with the model
        JTable table1 = new JTable(model);
        
        // Center the text in the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table1);
        
        // Add scroll pane to the frame
        statusFrame.add(scrollPane);
        
        // Make the frame visible
        statusFrame.setVisible(true);
    }
    */
    
    public static void updateActiveOrder(OrderList arg){
        showOrders = arg;
    }

    public static void updateOldOrder(OrderList arg){
        HistoryOrders = arg;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Screen screen = new Screen();
            screen.setVisible(true);
        });
    }


}