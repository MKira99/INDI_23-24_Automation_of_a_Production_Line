package MES_GUI;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.Box;
import javax.swing.BoxLayout;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;




public class Screen extends JFrame {

    public Screen() {
        // Create the frame
        JFrame frame = new JFrame("MES User Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        frame.setLocationRelativeTo(null);
        // Create a panel with vertical layout for the title and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Create and set the title label
        JLabel titleLabel = new JLabel("MES User Interface");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));  // Increased font size
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create the buttons
        JButton button1 = new JButton("Current Orders");
        button1.setFont(new Font("Arial", Font.PLAIN, 24));  // Increased font size
        button1.setMaximumSize(new Dimension(300, 80));  // Set a larger size
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when button 2 is clicked
                showOrdersStatusFrame();
            }
        });
        
        JButton button3 = new JButton("Machines Statistics");
        button3.setFont(new Font("Arial", Font.PLAIN, 24));  // Increased font size
        button3.setMaximumSize(new Dimension(300, 80));  // Set a larger size
        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when button 4 is clicked
                unloadedWorkPieces();
            }
        });
        
        // Add spacing around the buttons
        panel.add(Box.createRigidArea(new Dimension(0, 40))); // space above title
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 80))); // space between title and button 1
        panel.add(button1);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between buttons
        panel.add(button2);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between buttons
        panel.add(button3);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between buttons
        panel.add(button4);
        
        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);
        
        // Make the frame visible
        frame.setVisible(true);
    
    }
    // Method to show the frame for "Current Orders"
    private static void showOrdersFrame() {
        JFrame ordersFrame = new JFrame("Current Orders");
        ordersFrame.setSize(600, 400);
        ordersFrame.setLocationRelativeTo(null); // Center the new frame
        
        // Define the data with time required
        List<Object[]> orders = new ArrayList<>();
        orders.add(new Object[]{"1", "Product A", "10", 7.0});
        orders.add(new Object[]{"2", "Product B", "5", 2.5});
        orders.add(new Object[]{"3", "Product C", "8", 28.0});
        orders.add(new Object[]{"4", "Product D", "15", 13.0});
        
        // Sort the data based on the time required
        orders.sort(Comparator.comparingDouble(o -> (Double) o[3]));
        
        // Create a custom table model with column titles
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Order ID", "Produce Pieces", "Remaning Pieces", "Time Required","Total Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        // Add the sorted data to the table model
        for (Object[] order : orders) {
            model.addRow(order);
        }

        // Create a JTable with the model
        JTable table = new JTable(model);
        
        // Center the text in the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Sort the table by the "Time Required" column (index 3)
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(3, Comparator.comparingDouble(o -> (Double) o));
        table.setRowSorter(sorter);
        sorter.sort();
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add scroll pane to the frame
        ordersFrame.add(scrollPane);
        
        // Make the frame visible
        ordersFrame.setVisible(true);
    }
    
    // Method to show the frame for "Orders Status"
    private static void showOrdersStatusFrame() {
        JFrame statusFrame = new JFrame("Orders Status");
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
        model.addColumn("Order ID");
        model.addColumn("Number of Pieces");
        model.addColumn("Expected Waiting Time");
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
    private static void machinesStatistics() {
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
        /*searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchNumber = searchField.getText();
                int number = Integer.parseInt(searchNumber);
                int time = Integer.parseInt(searchNumber);

                Border squareBorder = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.decode("#000000"), 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                );




                OrderDetails orderDetails = getOrderDetails(number);
                TimeDetails timeDetails = getTimeDetails(time);
                // Perform search action with the 'number'
                // Retrieve the necessary data from the database
                int pieceProduced= orderDetails.getPieceProduced();
                int totalTime = timeDetails.getTotalTime();
                int Pt5s = timeDetails.getPT5s();
                int St6s = timeDetails.getSt6s();
                int St5s = timeDetails.getSt5s();
                int St3s = timeDetails.getSt3s();


                // Create a new window to display the order details
                orderDetailsFrame = new JFrame();
                orderDetailsFrame.setTitle("Order Details");
                orderDetailsFrame.setSize(480, 300);
                orderDetailsFrame.setResizable(false);
                orderDetailsFrame.setLocationRelativeTo(null);
                orderDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel resultPanel = new JPanel();
                resultPanel.setLayout(new GridLayout(10, 1, 10, 10)); // Adjust the spacing between labels




                JLabel pieceLabel = new JLabel("Piece on Production: " + pieceProduced);
                pieceLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text horizontally
                pieceLabel.setPreferredSize(new Dimension(150, 20)); // Reduce the height of the label
                pieceLabel.setBorder(squareBorder);
                resultPanel.add(pieceLabel);


                JLabel timeLabel = new JLabel("Total time spent: " +totalTime);
                timeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text horizontally
                timeLabel.setPreferredSize(new Dimension(150, 20)); // Reduce the height of the label
                timeLabel.setBorder(squareBorder);
                resultPanel.add(timeLabel);



                JLabel timeLabel1 = new JLabel("Engine Pt5s: " + Pt5s);
                timeLabel1.setHorizontalAlignment(SwingConstants.CENTER); // Center the text horizontally
                timeLabel1.setPreferredSize(new Dimension(150, 20)); // Reduce the height of the label
                timeLabel1.setBorder(squareBorder);
                resultPanel.add(timeLabel1);


                JLabel timeLabel2 = new JLabel("Engine St6s: " + St6s);
                timeLabel2.setHorizontalAlignment(SwingConstants.CENTER); // Center the text horizontally
                timeLabel2.setPreferredSize(new Dimension(150, 20)); // Reduce the height of the label
                timeLabel2.setBorder(squareBorder);
                resultPanel.add(timeLabel2);


                JLabel timeLabel3 = new JLabel("Engine St5s: " + St5s);
                timeLabel3.setHorizontalAlignment(SwingConstants.CENTER); // Center the text horizontally
                timeLabel3.setPreferredSize(new Dimension(150, 20)); // Reduce the height of the label
                timeLabel3.setBorder(squareBorder);
                resultPanel.add(timeLabel3);


                JLabel timeLabel4 = new JLabel("Engine St6s: " + St3s);
                timeLabel4.setHorizontalAlignment(SwingConstants.CENTER); // Center the text horizontally
                timeLabel4.setPreferredSize(new Dimension(150, 20)); // Reduce the height of the label
                timeLabel4.setBorder(squareBorder);
                resultPanel.add(timeLabel4);







                orderDetailsFrame.add(resultPanel);
                orderDetailsFrame.setVisible(true);

                orderDetailsFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Show the "Order Search" window when "Order Details" window is closed
                        setVisible(true);
                    }
                });
                orderDetailsFrame.setVisible(true);

                // Hide the "Order Search" window
                setVisible(false);
            }
        });*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Screen screen = new Screen();
            screen.setVisible(true);
        });
    }


}