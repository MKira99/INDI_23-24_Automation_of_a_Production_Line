package Others;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import Others.ConsolidatedOrderSystem.*;
import Others.DecisionOrder.*;

public class UDPServer {
    private static List<List<Order>> orderMatrix = new ArrayList<>();

    public static void main(String[] args) throws IOException {
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
            orderMatrix.add(orders);

            for (Order order : orders) {
                System.out.println(order);

                OrderSystem.addOrder(order);

                // Set order data in DataOrder
                DataOrder.setOrderData(order.getWorkpiece(), order.getQuantity(), order.getDueDate());
                DataOrder.printOrderData();

                Product product = new Product(order.getWorkpiece(), order.getQuantity());

                if (InventorySystem.checkHas(product, product.getQuantity()) == 1) {
                    System.out.println("Has Enough on Warehouse");
                } else {
                    System.out.println("Nao temos no armazem essas peças, mas temos gajas se quiseres.");
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

                // Process order with DataOrder and get the summary
                String response = processOrderWithDataOrder(nextOrder);
                System.out.println("Order Summary: " + response);
            }

            buf = new byte[65535];
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

    private static String processOrderWithDataOrder(Order order) {
        // Set order data in DataOrder
        DataOrder.setOrderData(order.getWorkpiece(), order.getQuantity(), order.getDueDate());
        return DataOrder.getOrderSummary();
    }
}