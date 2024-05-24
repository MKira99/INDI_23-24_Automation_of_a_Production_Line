package Others;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import Others.ConsolidatedOrderSystem.*;


public class Threader {
    
    public static class UDPServer implements Runnable{
        private static List<List<Order>> orderMatrix = new ArrayList<>();
        @Override
        public void run(){
            int port = 12345;
            try {
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
                    orderMatrix.add(orders);

                    for (Order order : orders) {
                        System.out.println(order);

                        OrderSystem.addOrder(order);

                        DataOrder.setOrderData(order.getWorkpiece(), order.getQuantity(), order.getDueDate());
                        DataOrder.printOrderData();

                        Product product = new Product(order.getWorkpiece(), order.getQuantity());

                        ////OPTIMIZAR ESTE CODIGO

                        if (InventorySystem.checkHas(product, product.getQuantity()) == 1) {
                            System.out.println("Has Enough on Warehouse");
                        } else {
                            System.out.println("Nao temos no armazem essas peças.");
                            Supplier Supplier1 = new Supplier(1, new int[]{30, 10}, 4);
                            Supplier Supplier2 = new Supplier(2, new int[]{45, 15}, 2);
                            Supplier Supplier3 = new Supplier(3, new int[]{55, 18}, 1);
                            PurchaseSystem.addSupplier(Supplier1);
                            PurchaseSystem.addSupplier(Supplier2);
                            PurchaseSystem.addSupplier(Supplier3);
                            PurchaseOrder Ordem1 = new PurchaseOrder(Supplier1, 2, 4);
                            PurchaseSystem.createPurchaseOrder(Ordem1);
                            System.out.println("ORDEM EFETUADA :" + PurchaseSystem.getPurchaseOrdersForSupplier(Supplier1).get(0));
                        }
                    }

                    printOrderMatrix();

                    // Decide which order to process next based on the smallest due date
                    Order nextOrder = DecisionOrder.decideOrder(orderMatrix);
                    if (nextOrder != null) {
                        System.out.println("Next order to process: " + nextOrder);
                    }
                    buf = new byte[65535];
                    ds.close();



                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
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
    
                        /*System.out.println("clientNameId: " + clientNameId);
                        System.out.println("number: " + number);
                        System.out.println("workPiece: " + workPiece);
                        System.out.println("quantity: " + quantity);
                        System.out.println("dueDate: " + dueDate);
                        System.out.println("latePen: " + latePen);
                        System.out.println("earlyPen: " + earlyPen);*/
    
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
        private static void printOrderMatrix() {
            System.out.println("Order Matrix:");
            for (List<Order> orderList : orderMatrix) {
                for (Order order : orderList) {
                    System.out.println(order);
                }
                System.out.println("-----");
            }
        }
    }

    public static class TCPServer implements Runnable{

        public void run() {

            try {
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
                    String requestString = new String(requestBytes);

                    // Parse the JSON string as a JSON object
                    JSONObject requestJson = new JSONObject(requestString);

                    // Print the request JSON object
                    //System.out.println("Received request: " + requestJson);

                    // Create a JSON object to store the response data
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("status", "OK");

                    // Convert the response JSON object to a JSON string
                    String responseString = responseJson.toString();

                    // Create an output stream to send data to the client
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // Send the response string to the client
                    outputStream.write(responseString.getBytes());

                    OrderToMES myOrder = new OrderToMES();
                    myOrder.setField1(requestJson.getInt("Piece"));
                    myOrder.setField2(requestJson.getInt("Quantity"));
                    myOrder.setField3(requestJson.getInt("Day"));


                    System.out.println(myOrder.getField1() + " " + myOrder.getField2() + " " + myOrder.getField3());
                    //System.out.println(myOrder.getField2());
                    //System.out.println(myOrder.getField3());
                    // Close the streams and the client socket
                    serverSocket.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}





}
