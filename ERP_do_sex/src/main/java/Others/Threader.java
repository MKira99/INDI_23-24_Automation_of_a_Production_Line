package Others;

import java.net.*;
import java.sql.SQLException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.json.JSONObject;

import Others.DataOrder.*;
import Others.OrderDatabase.OrderDb;
import Others.OrderDatabase.OrderSystemDb;

public class Threader {

    public static class UDPServer implements Runnable {
        public static List<DataOrder.Order> allOrders = new ArrayList<>();
        public static List<OrderListener> listeners = new ArrayList<>();
        public static List<List<DataOrder.Order>> orderMatrix = new ArrayList<>();
        public static List<DataOrder.OrderResult> processedOrders = new ArrayList<>(); // Lista para manter todas as ordens processadas
        //public static List<List<OrderDb>> orderDbMatrix = new ArrayList<>();
        public static List<DataOrder.Order> ordersended = new ArrayList<>();
        public static List<DataOrder.Order> receivedOrders = new ArrayList<>();
        public static boolean firstTime=true; 
        public static boolean dbIncomplete=false;
        public static boolean dbNotSended=false;


        @Override
        public void run() {
            try {
                int port = 24680;
                Order orderNormalDb;
                List<DataOrder.Order> ordersDbList = new ArrayList<>();
                List<DataOrder.Order> allOrders = new ArrayList<>(); // Lista para manter todas as ordens recebidas
                int[] currentDay = {5}; // Todas as ordens só podem começar a partir do dia 5
                byte[] receiveData = new byte[1024];
                Map<String, Double> rawMaterialCosts = new HashMap<>();
                rawMaterialCosts.put("P1", 30.0);
                rawMaterialCosts.put("P2", 10.0);
    
                while (true) {
                    try{
                        if((DatabaseERP.isTableEmpty()) || (!DatabaseERP.isTableEmpty() && firstTime==false)){
                            DatagramSocket socket = new DatagramSocket(port);
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            boolean send2mes = allOrders.isEmpty();
            
                            System.out.println("Waiting for the packet...");
                            socket.receive(receivePacket);
                            String xmlData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                            socket.close();
            
                            List<DataOrder.Order> newOrders = DataOrder.parseOrders(xmlData);
                            allOrders.addAll(newOrders); // Adiciona novas ordens à lista de todas as ordens

                            for (Order order : newOrders){
                                try {
                                    System.out.println("Inserting order into database: " + order);
                                    DatabaseERP.insertOrder(order.orderId, order.number, order.clientName, order.workPiece, order.quantity, order.dueDate, order.latePen, order.earlyPen);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    continue; // Skip this iteration if the connection fails
                                }
                            }
                            
                            receivedOrders.addAll(newOrders);

                            // Calcular e imprimir a quantidade total de peças iniciais necessárias
                            DataOrder.calculateTotalInitialPieces(allOrders);
            
                            // Preparar estoque inicial para todas as ordens e calcular o custo total
                            DataOrder.prepareInitialStock(allOrders);
                            
                            notifyListeners(allOrders);
                            DataOrder.printOrderStatus(allOrders, processedOrders);

                            // Processa as ordens uma a uma
                            // Notify listeners about new orders
                            if(send2mes == true){
                                boolean hasMoreOrders = DataOrder.processNextOrder(allOrders, processedOrders, currentDay, rawMaterialCosts);                       
                            }
                            // Imprime as ordens processadas e as que ainda não foram processadas
                            DataOrder.printOrderStatus(allOrders, processedOrders);
                        }
                        else{
                            System.out.println("INNNNN\n\n");
                            ArrayList<OrderDb> orders = (ArrayList<OrderDb>) OrderSystemDb.getAllOrders();
                            for (OrderDb order : orders) {
                                System.out.println("ORDERSDB" + order);
                                if(order.orderCost==null || order.startDate==null || order.endDate==null){
                                    dbIncomplete=true;
                                    orderNormalDb= new Order(order.orderNumber, order.clientName, order.workpiece, order.quantity, order.dueDate, order.latePen, order.earlyPen);
                                    ordersDbList.add(orderNormalDb);
                                }
                            }
                            // Se não chegou a atualizar os novos dados na base de dados
                            if(dbIncomplete){
                                boolean send2mes = allOrders.isEmpty();


                                allOrders.addAll(ordersDbList); // Adiciona novas ordens à lista de todas as ordens

                                receivedOrders.addAll(ordersDbList);

                                // Calcular e imprimir a quantidade total de peças iniciais necessárias
                                DataOrder.calculateTotalInitialPieces(allOrders);

                                // Preparar estoque inicial para todas as ordens e calcular o custo total
                                DataOrder.prepareInitialStock(allOrders);
                                
                                // Processa as ordens uma a uma
                                    // Notify listeners about new orders
                                notifyListeners(allOrders);
                                DataOrder.printOrderStatus(allOrders, processedOrders);

                                if (send2mes) {
                                    boolean hasMoreOrders = DataOrder.processNextOrder(allOrders, processedOrders, currentDay, rawMaterialCosts);
                                }
                                
                                System.out.println("Finishedddd\n\n");

                                // Imprime as ordens processadas e as que ainda não foram processadas
                                DataOrder.printOrderStatus(allOrders, processedOrders);
                            }
                            // Apenas falta enviar para o MES
                            else{
                                for (OrderDb order : orders) {
                                    if(order.orderCost!=null && order.startDate!=null && order.endDate!=null && !order.sendedMes){
                                        orderNormalDb= new Order(order.orderNumber, order.clientName, order.workpiece, order.quantity, order.dueDate, order.latePen, order.earlyPen);
                                        ordersDbList.add(orderNormalDb);

                                        JSONObject response = order.toJSONDb();
                                        // Envia o JSON para o MES
                                        TCPClient.main(response);

                                        // Update sendedMes variable in database
                                        try {
                                            System.out.println("Setting sendedMes variable true for order: " + order.orderId);
                                            DatabaseERP.updateSendedMes(order.orderId);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        
                                    }
                                }
                                allOrders.addAll(ordersDbList); // Adiciona novas ordens à lista de todas as ordens
                                
                                // Notify listeners about new orders
                                notifyListeners(allOrders);
                            }

                        }
                        /*if(!DatabaseERP.isTableEmpty() && (dbIncomplete || dbNotSended || firstTime)){
                            firstTime=true;
                        }
                        else{
                            firstTime=false;
                            dbIncomplete=false;
                        }*/
                        firstTime=false;
                        receiveData = new byte[65535];
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }    
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        private static void notifyListeners(List<DataOrder.Order> orders){
            for (OrderListener listener : listeners) {
                listener.onNewOrders(orders);
            }
        }

        public static void addOrderListener(OrderListener listener) {
            listeners.add(listener);
        }

    }


    public static class GUI implements Runnable {
        private static JFrame frame;
        private static JTable processedOrdersTable;
        private static JTable receivedOrdersTable;
        private static DefaultTableModel processedOrdersModel;
        private static DefaultTableModel receivedOrdersModel;
        private static JTextArea initialStockTextArea;

        @Override
        public void run() {
            frame = new JFrame("Order Processing System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            // Processed Orders Table
            String[] processedColumns = {"Order ID", "Work Piece", "Quantity", "Start Date", "End Date", "Cost"};
            processedOrdersModel = new DefaultTableModel(processedColumns, 0);
            processedOrdersTable = new JTable(processedOrdersModel);
            JScrollPane processedScrollPane = new JScrollPane(processedOrdersTable);

            // Received Orders Table
            String[] receivedColumns = {"Order ID", "Number", "Client Name", "Work Piece", "Quantity", "Due Date", "Late Penalty", "Early Penalty"};
            receivedOrdersModel = new DefaultTableModel(receivedColumns, 0);
            receivedOrdersTable = new JTable(receivedOrdersModel);
            JScrollPane receivedScrollPane = new JScrollPane(receivedOrdersTable);

            // Initial Stock Information
            initialStockTextArea = new JTextArea(5, 20);
            initialStockTextArea.setEditable(false);
            JScrollPane initialStockScrollPane = new JScrollPane(initialStockTextArea);
            initialStockScrollPane.setBorder(BorderFactory.createTitledBorder("Initial Stock"));

            // Layout setup
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, receivedScrollPane, processedScrollPane);
            splitPane.setDividerLocation(300);

            frame.add(splitPane, BorderLayout.CENTER);
            frame.add(initialStockScrollPane, BorderLayout.SOUTH);
            frame.setVisible(true);

            // Register as a listener for new orders
            UDPServer.addOrderListener(new OrderUpdateListener());

            // Display initial unprocessed orders and initial stock
            updateOrderDisplays();
            updateInitialStockDisplays();
        }

        public void setVisible(boolean visible) {
            if (frame != null) {
                frame.setVisible(visible);
            }
        }

        private static class OrderUpdateListener implements OrderListener {
            @Override
            public void onNewOrders(List<DataOrder.Order> orders) {
                SwingUtilities.invokeLater(() -> {
                    updateOrderDisplays();
                    updateInitialStockDisplays();
                });
            }
        }

        public static void updateOrderDisplays() {
            List<DataOrder.Order> allOrders = UDPServer.allOrders;
            List<DataOrder.OrderResult> processedOrders = UDPServer.processedOrders;
            List<DataOrder.Order> receivedOrders = UDPServer.receivedOrders;

            // Clear existing rows
            processedOrdersModel.setRowCount(0);
            receivedOrdersModel.setRowCount(0);

            // Add processed orders to the table
            for (DataOrder.OrderResult processedOrder : processedOrders) {
                processedOrdersModel.addRow(new Object[]{
                    processedOrder.orderId,
                    processedOrder.workPiece,
                    processedOrder.quantity,
                    processedOrder.startDate,
                    processedOrder.endDate,
                    processedOrder.cost
                });
            }

            // Add received orders to the table
            for (DataOrder.Order order : receivedOrders) {
                receivedOrdersModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.number,
                    order.clientName,
                    order.workPiece,
                    order.quantity,
                    order.dueDate,
                    order.latePen,
                    order.earlyPen
                });
            }
        }

        public static void updateInitialStockDisplays() {
            List<SupplierUsage> supplierUsages = DataOrder.getSupplierUsages();
            StringBuilder stockInfo = new StringBuilder();

            for (SupplierUsage usage : supplierUsages) {
                stockInfo.append(String.format("Supplier: %s, Piece: %s, Quantity: %d, Price per Piece: %.2f%n",
                        usage.supplierName, usage.piece, usage.quantity, usage.pricePerPiece));
            }

            initialStockTextArea.setText(stockInfo.toString());
        }
    }


    public static class TCPServer implements Runnable {

        private static List<DataOrder.Order> allOrders = UDPServer.allOrders;
        private static List<DataOrder.OrderResult> processedOrders = new ArrayList<>();
        private static int[] currentDay = {5};
        private static Map<String, Double> rawMaterialCosts = new HashMap<>();

        @Override
        public void run() {
            try {
                rawMaterialCosts.put("P1", 30.0);
                rawMaterialCosts.put("P2", 10.0);

                // Create a server socket that listens on port 4999
                ServerSocket serverSocket = new ServerSocket(4999);
                System.out.println("Server started. Listening on port 4999...");

                InetAddress inetAddress = InetAddress.getLocalHost();
                String ipAddress = inetAddress.getHostAddress();
                System.out.println("Your IP address is: " + ipAddress);

                // Listen for incoming connections and handle them
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                    // Create an input stream to receive data from the client
                    InputStream inputStream = clientSocket.getInputStream();

                    // Read the request from the client as a byte array
                    byte[] requestBytes = new byte[1024];
                    inputStream.read(requestBytes);

                    // Convert the byte array to a JSON string
                    String requestString = new String(requestBytes).trim();

                    // Parse the JSON string as a JSON object
                    JSONObject requestJson = new JSONObject(requestString);

                    // Print the request JSON object
                    System.out.println("Received request: " + requestJson);

                    // Process the next order
                    boolean hasMoreOrders = DataOrder.processNextOrder(allOrders, processedOrders, currentDay, rawMaterialCosts);
                    DataOrder.OrderResult nextOrderResult = hasMoreOrders ? processedOrders.get(processedOrders.size() - 1) : null;

                    // Print the next order to be processed
                    System.out.println("Next order to be sent: " + (nextOrderResult != null ? nextOrderResult.toJSON() : "No orders to process"));

                    // Create a JSON object to store the response data
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("status", hasMoreOrders ? "OK" : "No more orders");
                    responseJson.put("orderResult", nextOrderResult != null ? nextOrderResult.toJSON() : new JSONObject());

                    // Convert the response JSON object to a JSON string
                    String responseString = responseJson.toString();

                    // Create an output stream to send data to the client
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // Send the response string to the client
                    outputStream.write(responseString.getBytes());

                    // Close the streams and the client socket
                    clientSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public interface OrderListener {
        void onNewOrders(List<DataOrder.Order> orders);
    }
}