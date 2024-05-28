package Others;

import java.net.*;
import java.sql.SQLException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

import Others.DataOrder.*;

public class Threader {

    public static class UDPServer implements Runnable {
        public static List<Order> allOrders = new ArrayList<>();
        public static List<OrderListener> listeners = new ArrayList<>();
        public static List<List<Order>> orderMatrix = new ArrayList<>();
        public static List<OrderResult> processedOrders = new ArrayList<>(); // Lista para manter todas as ordens processadas
        //public static List<List<OrderDb>> orderDbMatrix = new ArrayList<>();
        public static List<Order> ordersended = new ArrayList<>();
        public static boolean firstTime=true; 

        @Override
        public void run() {
            try {
                int port = 24680;
                List<Order> allOrders = new ArrayList<>(); // Lista para manter todas as ordens recebidas
                int[] currentDay = {5}; // Todas as ordens só podem começar a partir do dia 5
    
                Map<String, Double> rawMaterialCosts = new HashMap<>();
                rawMaterialCosts.put("P1", 30.0);
                rawMaterialCosts.put("P2", 10.0);
    
                while (true) {
                    DatagramSocket socket = new DatagramSocket(port);
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    
                    System.out.println("Waiting for the packet...");
                    socket.receive(receivePacket);
                    String xmlData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    socket.close();
    
                    List<Order> newOrders = DataOrder.parseOrders(xmlData);
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
    
                    // Calcular e imprimir a quantidade total de peças iniciais necessárias
                    DataOrder.calculateTotalInitialPieces(allOrders);
    
                    // Preparar estoque inicial para todas as ordens e calcular o custo total
                    DataOrder.prepareInitialStock(allOrders);
                    
                    // Notify listeners about new orders
                    notifyListeners(allOrders);

                    // Processa as ordens uma a uma
                    while (true) {
                        DataOrder.printOrderStatus(allOrders, processedOrders);
                        boolean hasMoreOrders = DataOrder.processNextOrder(allOrders, processedOrders, currentDay, rawMaterialCosts);
                        if (!hasMoreOrders) {
                            break;
                        }
                    }

                    // Imprime as ordens processadas e as que ainda não foram processadas
                    DataOrder.printOrderStatus(allOrders, processedOrders);
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        public static void notifyListeners(List<Order> orders) {
            for (OrderListener listener : listeners) {
                listener.onNewOrders(orders);
            }
        }

        public static void addOrderListener(OrderListener listener) {
            listeners.add(listener);
        }

    }

    /*public static class GUI implements Runnable {
        public void run() {
            // Launch the GUI
            SwingUtilities.invokeLater(() -> {
                ProductionGUI gui = new ProductionGUI();
                UDPServer.addOrderListener(gui);
                gui.setVisible(true);
                
            });
        }
    }*/

    public static class TCPServer implements Runnable {

        private static List<Order> allOrders = UDPServer.allOrders;
        private static List<OrderResult> processedOrders = new ArrayList<>();
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
                    OrderResult nextOrderResult = hasMoreOrders ? processedOrders.get(processedOrders.size() - 1) : null;

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
}