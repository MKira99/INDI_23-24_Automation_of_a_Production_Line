package Others;

import java.net.*;
import java.sql.SQLException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import Others.ConsolidatedOrderSystem.*;
import Others.DataOrder.*;
//import static Others.DatabaseERP.*;

public class Threader {

    public static class UDPServer implements Runnable {
        private static List<Order> allOrders = new ArrayList<>();
        public static List<OrderListener> listeners = new ArrayList<>();
        public static List<List<Order>> orderMatrix = new ArrayList<>();
        public static List<Order> ordersended = new ArrayList<>();

        @Override
        public void run() {
            try {
                int port = 24680;
                DatagramSocket ds = new DatagramSocket(port);
                byte[] buf = new byte[65535];
                DatagramPacket DpReceive = null;
    
                while (true) {
                    DpReceive = new DatagramPacket(buf, buf.length);
                    ds.receive(DpReceive);
                    System.out.println("Received: " + DpReceive);
                    String inp = new String(buf, 0, DpReceive.getLength());
                    System.out.println("string: " + inp);
    
                    List<Order> orders = parseOrders(inp);
                    if (orders == null || orders.isEmpty()) {
                        System.out.println("No valid orders found.");
                        continue;
                    }
    
                    synchronized (allOrders) {
                        for (Order order : orders) {
                            System.out.println("Processing order: " + order);
                            adjustDueDateIfNeeded(order); // Ajusta a data de entrega se necessário
                            if (!isOrderDuplicated(order)) {
                                System.out.println("Order is not duplicated: " + order);
                                allOrders.add(order);
    
                                OrderSystem.addOrder(order);
    
                                // Set order data in DataOrder
                                DataOrder.setOrderData(order.getWorkpiece(), order.getQuantity(), order.getDueDate());
    
                                JSONObject summary = DataOrder.getOrderSummary();
                                int finalDateDays = summary.getInt("DateEnd");
                                String finalDateStr = ProductionGUI.convertDaysToDate(finalDateDays);
    
                                DataOrder.printOrderData();
    
                                Product product = new Product(order.getWorkpiece(), order.getQuantity());
    
                                if (InventorySystem.checkHas(product, product.getQuantity()) == 1) {
                                    System.out.println("Has Enough on Warehouse");
                                } else {
                                    System.out.println("Nao temos no armazem essas peças.");
    
                                    // Determine the best supplier for the initial piece
                                    String initialPiece = DataOrder.determineInitialPiece(order.getWorkpiece());
                                    Supplier bestSupplier = Supplier.getBestSupplier(initialPiece, order.getQuantity(), order.getDueDate());
                                    if (bestSupplier != null) {
                                        System.out.println("Best supplier found: " + bestSupplier.getName());
                                    } else {
                                        System.out.println("No suitable supplier found for initial piece: " + initialPiece);
                                    }
                                    double cost = bestSupplier.getPricePerPiece() * bestSupplier.getMinimumOrder();
                                    /*try {
                                        System.out.println("Inserting order cost into database: " + cost + " for order: " + order.getOrderNumber());
                                        insertOrderCost(cost, order.getOrderNumber());
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        continue; // Skip this iteration if the connection fails
                                    }*/
                                }
    
                                // Save order to JSON
                                String clientID = DataOrder.generateClientID(order);
                                System.out.println("Generated clientID: " + clientID);
                                DataOrder.saveOrderToJson(order, clientID);
                                System.out.println("Order saved to JSON for clientID: " + clientID);
                            } else {
                                System.out.println("Order is duplicated and will not be processed: " + order);
                            }
    
                        }
                    }
    
                    printOrders();
    
                    // Notify listeners about new orders
                    notifyListeners(orders);

                    Order nextOrder = DecisionOrder.decideOrder(orderMatrix);

                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    printOrderMatrix();
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");

                    orderMatrix.add(orders);

                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    printOrderMatrix();
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");

                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    printOrderMatrix();
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");

                    nextOrder = DecisionOrder.decideOrder(orderMatrix);

                    if (nextOrder != null) {
                        System.out.println("Next order to process: " + nextOrder);

                        // Process order with DataOrder and get the summary
                        JSONObject response = processOrderWithDataOrder(nextOrder);

                        String clientID = DataOrder.generateClientID(nextOrder);
                        System.out.println("ClientID: " + clientID);
                        DataOrder.saveOrderToJson(nextOrder, clientID);
                        //DEBUG
                        response.put("DateEnd", response.get("DateStart"));
                        TCPClient.main(response);
                        
                        synchronized (this) {
                            try {
                                this.wait(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }


                    } else {
                        System.out.println("No next order to process.");
                    }


    
                    buf = new byte[65535];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        private static void adjustDueDateIfNeeded(Order order) {
            int originalDueDate = order.getDueDate();
            int processingTime = DataOrder.calculateProcessingTimeForOrder(order); // Tempo de processamento da ordem atual em dias
            int newDueDate = originalDueDate;
            boolean dateTaken;
        
            do {
                dateTaken = false;
                for (Order existingOrder : allOrders) {
                    int existingProcessingTime = DataOrder.calculateProcessingTimeForOrder(existingOrder); // Tempo de processamento da ordem existente em dias
                    int existingStart = existingOrder.getDueDate();
                    int existingEnd = existingStart + existingProcessingTime - 1; // Data final da ordem existente
                    int orderEnd = newDueDate + processingTime - 1; // Data final da nova ordem
        
                    // Verifica se as datas se sobrepõem
                    if ((newDueDate >= existingStart && newDueDate <= existingEnd) || 
                        (orderEnd >= existingStart && orderEnd <= existingEnd) || 
                        (newDueDate <= existingStart && orderEnd >= existingEnd)) {
                        newDueDate = existingEnd + 1; // Ajusta para o próximo dia após a ordem existente
                        dateTaken = true;
                        break;
                    }
                }
            } while (dateTaken);
        
            if (originalDueDate != newDueDate) {
                order.setDueDate(newDueDate); // Atualiza a data de entrega da ordem
                System.out.println("Adjusted due date for order " + order.getOrderNumber() + " from " + originalDueDate + " to " + newDueDate);
            }
        }
    
        private static boolean isOrderDuplicated(Order order) {
            synchronized (allOrders) {
                for (Order existingOrder : allOrders) {
                    if (existingOrder.equals(order)) {
                        return true;
                    }
                }
            }
            return false;
        }
    
        private static List<Order> parseOrders(String xml) {
            List<Order> orders = new ArrayList<>();
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(xml)));
    
                NodeList clientNodes = doc.getElementsByTagName("Client");
                NodeList orderNodes = doc.getElementsByTagName("Order");
    
                if (clientNodes.getLength() > 0) {
                    String clientNameId = clientNodes.item(0).getAttributes().getNamedItem("NameId").getNodeValue();
    
                    for (int i = 0; i < orderNodes.getLength(); i++) {
                        Element orderElement = (Element) orderNodes.item(i);
                        String number = orderElement.getAttribute("Number");
                        String workPiece = orderElement.getAttribute("WorkPiece");
                        String quantity = orderElement.getAttribute("Quantity");
                        String dueDate = orderElement.getAttribute("DueDate");
                        String latePen = orderElement.getAttribute("LatePen");
                        String earlyPen = orderElement.getAttribute("EarlyPen");
    
                        Order order = new Order(clientNameId, Integer.parseInt(number), workPiece, Integer.parseInt(quantity),
                                Integer.parseInt(dueDate), Integer.parseInt(latePen), Integer.parseInt(earlyPen));
                        orders.add(order);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return orders;
        }

        private static void printOrders() {
            System.out.println("Order List:");
            for (Order order : allOrders) {
                System.out.println(order);
            }
            System.out.println("-----");
        }

        private static void printOrderMatrix() {
            System.out.println("Order Matrix:");
            for (List<Order> orderList : orderMatrix) {
                for (Order order : orderList) {
                    System.out.println(order);
                }
                System.out.println("-----");
            }
        }

        private static void notifyListeners(List<Order> orders) {
            for (OrderListener listener : listeners) {
                listener.onNewOrders(orders);
            }
        }

        public static void addOrderListener(OrderListener listener) {
            listeners.add(listener);
        }

        public static synchronized Order getNextOrder() {
            if (allOrders.isEmpty()) {
                return null;
            }

            Order nextOrder = allOrders.get(0);
            for (Order order : allOrders) {
                if (order.getDueDate() < nextOrder.getDueDate()) {
                    nextOrder = order;
                }
            }
            return nextOrder;
        }
    
        private static JSONObject processOrderWithDataOrder(Order nextOrder) {
            DataOrder.setOrderData(nextOrder.getWorkpiece(), nextOrder.getQuantity(), nextOrder.getDueDate());
            return DataOrder.getOrderSummary();
        }
    }

    public static class GUI implements Runnable {
        public void run() {
            // Launch the GUI
            SwingUtilities.invokeLater(() -> {
                ProductionGUI gui = new ProductionGUI();
                UDPServer.addOrderListener(gui);
                gui.setVisible(true);
                
            });
        }
    }

    public static class TCPServer implements Runnable {

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(12346);
                System.out.println("TCP Server started on port 12346");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        
    }
}