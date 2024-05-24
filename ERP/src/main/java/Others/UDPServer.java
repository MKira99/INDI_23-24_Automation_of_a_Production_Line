package Others;


import java.net.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import Others.ConsolidatedOrderSystem.*;



public class UDPServer {
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

            Order order = parseOrder(inp);

            System.out.println(order);

            buf = new byte[65535];

            OrderSystem.addOrder(order);

            DataOrder.setOrderData(order.getWorkpiece(), order.getQuantity(), order.getDueDate());
            DataOrder.printOrderData();

            // The rest of your logic here
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
    }

    private static Order parseOrder(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));

            NodeList clientNodes = doc.getElementsByTagName("Client");
            NodeList orderNodes = doc.getElementsByTagName("Order");

            if (clientNodes.getLength() > 0 && orderNodes.getLength() > 0) {
                String clientNameId = clientNodes.item(0).getAttributes().getNamedItem("NameId").getNodeValue();
                String number = orderNodes.item(0).getAttributes().getNamedItem("Number").getNodeValue();
                String workPiece = orderNodes.item(0).getAttributes().getNamedItem("WorkPiece").getNodeValue();
                String quantity = orderNodes.item(0).getAttributes().getNamedItem("Quantity").getNodeValue();
                String dueDate = orderNodes.item(0).getAttributes().getNamedItem("DueDate").getNodeValue();
                String latePen = orderNodes.item(0).getAttributes().getNamedItem("LatePen").getNodeValue();
                String earlyPen = orderNodes.item(0).getAttributes().getNamedItem("EarlyPen").getNodeValue();

                System.out.println("clientNameId: " + clientNameId);
                System.out.println("number: " + number);
                System.out.println("workPiece: " + workPiece);
                System.out.println("quantity: " + quantity);
                System.out.println("dueDate: " + dueDate);
                System.out.println("latePen: " + latePen);
                System.out.println("earlyPen: " + earlyPen);

                return new Order(clientNameId, Integer.parseInt(number), workPiece, Integer.parseInt(quantity),
                        Integer.parseInt(dueDate), Integer.parseInt(latePen), Integer.parseInt(earlyPen));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}