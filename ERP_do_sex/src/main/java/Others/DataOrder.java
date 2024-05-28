package Others;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOrder {

    public static class Order {
        String number;
        String clientName;
        String workPiece;
        String quantity;
        String dueDate;
        String latePen;
        String earlyPen;
        String orderId;

        public Order(String number, String clientName, String workPiece, String quantity, String dueDate, String latePen, String earlyPen) {
            this.number = number;
            this.clientName = clientName;
            this.workPiece = workPiece;
            this.quantity = quantity;
            this.dueDate = dueDate;
            this.latePen = latePen;
            this.earlyPen = earlyPen;
            this.orderId = clientName.replaceAll("\\s+", "") + "_" + number;
        }

        public String getOrderId() {
            return orderId;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderId='" + orderId + '\'' +
                    ", number='" + number + '\'' +
                    ", clientName='" + clientName + '\'' +
                    ", workPiece='" + workPiece + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", dueDate='" + dueDate + '\'' +
                    ", latePen='" + latePen + '\'' +
                    ", earlyPen='" + earlyPen + '\'' +
                    '}';
        }
    }

    public static class Transformation {
        String startingPiece;
        String producedPiece;
        String tool;
        int processingTime;

        public Transformation(String startingPiece, String producedPiece, String tool, int processingTime) {
            this.startingPiece = startingPiece;
            this.producedPiece = producedPiece;
            this.tool = tool;
            this.processingTime = processingTime;
        }
    }

    public static class OrderResult {
        String orderId;
        String workPiece;
        int quantity;
        int startDate;
        int endDate;
        double cost;

        static ArrayList<OrderResult> orders = new ArrayList<OrderResult>();

        public OrderResult(String orderId, String workPiece, int quantity, int startDate, int endDate, double cost) {
            this.orderId = orderId;
            this.workPiece = workPiece;
            this.quantity = quantity;
            this.startDate = startDate;
            this.endDate = endDate;
            this.cost = cost;
        }

        public static void addOrderFinal(ArrayList<OrderResult> processedOrders) {
            orders=processedOrders;
            System.out.println("ORDERSSSSSSSSSSSSSS" + orders);
        }

        // Método para retornar todas as ordens
        public static List<OrderResult> getAllFinalOrders() {
            return new ArrayList<>(orders); // Retorna uma cópia da lista para evitar modificações externas
        }

        public String getOrderId() {
            return orderId;
        }
        
        public int getEndDate() {
            return endDate;
        }

        public double getCost(){
            return cost;
        }
        
        public JSONObject toJSON() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", orderId);
            jsonObject.put("workPiece", workPiece);
            jsonObject.put("quantity", quantity);
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("cost", cost);
            return jsonObject;
        }
    }

    public static class Supplier {
        String name;
        String piece;
        int minimumOrder;
        double pricePerPiece;
        int deliveryTime;

        public Supplier(String name, String piece, int minimumOrder, double pricePerPiece, int deliveryTime) {
            this.name = name;
            this.piece = piece;
            this.minimumOrder = minimumOrder;
            this.pricePerPiece = pricePerPiece;
            this.deliveryTime = deliveryTime;
        }
    }

    public static class SupplierUsage {
        String supplierName;
        String piece;
        int quantity;
        double pricePerPiece;

        public SupplierUsage(String supplierName, String piece, int quantity, double pricePerPiece) {
            this.supplierName = supplierName;
            this.piece = piece;
            this.quantity = quantity;
            this.pricePerPiece = pricePerPiece;
        }

        @Override
        public String toString() {
            return "SupplierUsage{" +
                    "supplierName='" + supplierName + '\'' +
                    ", piece='" + piece + '\'' +
                    ", quantity=" + quantity +
                    ", pricePerPiece=" + pricePerPiece +
                    '}';
        }
    }



    // Definindo a tabela de transformações
    public static List<Transformation> transformations = new ArrayList<>();
    static {
        transformations.add(new Transformation("P1", "P3", "T1", 45));
        transformations.add(new Transformation("P3", "P4", "T2", 15));
        transformations.add(new Transformation("P3", "P4", "T3", 25));
        transformations.add(new Transformation("P4", "P5", "T4", 25));
        transformations.add(new Transformation("P4", "P6", "T2", 25));
        transformations.add(new Transformation("P4", "P7", "T3", 15));
        transformations.add(new Transformation("P2", "P8", "T1", 45));
        transformations.add(new Transformation("P8", "P7", "T6", 15));
        transformations.add(new Transformation("P8", "P9", "T5", 45));
    }

    public static List<Supplier> loadSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(new Supplier("SupplierA", "P1", 16, 30.0, 4));
        suppliers.add(new Supplier("SupplierA", "P2", 16, 10.0, 4));
        suppliers.add(new Supplier("SupplierB", "P1", 8, 45.0, 2));
        suppliers.add(new Supplier("SupplierB", "P2", 8, 15.0, 2));
        suppliers.add(new Supplier("SupplierC", "P1", 4, 55.0, 1));
        suppliers.add(new Supplier("SupplierC", "P2", 4, 18.0, 1));
        return suppliers;
    }

    public static List<Order> parseOrders(String xmlData) {
        List<Order> orders = new ArrayList<>();
        try {
            // Parse the XML data
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
            Document doc = builder.parse(input);

            // Extract orders and store them in a list
            NodeList documentNode = doc.getElementsByTagName("DOCUMENT").item(0).getChildNodes();
            String clientName = "";
            for (int i = 0; i < documentNode.getLength(); i++) {
                if (documentNode.item(i) instanceof Element) {
                    Element element = (Element) documentNode.item(i);
                    if (element.getTagName().equals("Client")) {
                        clientName = element.getAttribute("NameId");
                    } else if (element.getTagName().equals("Order")) {
                        String number = element.getAttribute("Number");
                        String workPiece = element.getAttribute("WorkPiece");
                        String quantity = element.getAttribute("Quantity");
                        String dueDate = element.getAttribute("DueDate");
                        String latePen = element.getAttribute("LatePen");
                        String earlyPen = element.getAttribute("EarlyPen");

                        Order order = new Order(number, clientName, workPiece, quantity, dueDate, latePen, earlyPen);
                        orders.add(order);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static double calculateProcessingTimeInDays(String workPiece, int quantity, boolean isFirstTransformation) {
        double totalTimeInSeconds = 0;
        for (Transformation t : transformations) {
            if (t.producedPiece.equals(workPiece)) {
                if (isFirstTransformation) {
                    totalTimeInSeconds += Math.ceil((double) t.processingTime * quantity / 6.0);
                    isFirstTransformation = false;
                } else {
                    totalTimeInSeconds += Math.ceil((double) t.processingTime * quantity / 3.0);
                }
                totalTimeInSeconds += calculateProcessingTimeInDays(t.startingPiece, quantity, isFirstTransformation) * 60; // Convert days back to seconds for consistent addition
                break;
            }
        }
        return totalTimeInSeconds / 60.0; // Convert total time from seconds to days
    }

    public static void prepareInitialStock(List<Order> orders) {
        Map<String, Integer> initialStock = new HashMap<>();

        for (Order order : orders) {
            String workPiece = order.workPiece;
            int quantity = Integer.parseInt(order.quantity);
            accumulateInitialPieces(initialStock, workPiece, quantity, new HashMap<>());
        }

        double totalCost = 0;
        // Agora, comprar todas as peças necessárias e calcular os custos
        List<Supplier> suppliers = loadSuppliers();

        for (Map.Entry<String, Integer> entry : initialStock.entrySet()) {
            String piece = entry.getKey();
            int requiredQuantity = entry.getValue();

            System.out.println("Total quantity needed for " + piece + ": " + requiredQuantity);

            for (Supplier supplier : suppliers) {
                if (supplier.piece.equals(piece)) {
                    // Compra a quantidade necessária, garantindo que seja pelo menos o pedido mínimo
                    totalCost += requiredQuantity * supplier.pricePerPiece;
                    System.out.println("Bought " + requiredQuantity + " pieces of " + piece + " from " + supplier.name + " at cost " + (requiredQuantity * supplier.pricePerPiece));
                    break;
                }
            }
        }

        System.out.println("Total cost for initial stock: " + totalCost);
    }

    public static void accumulateInitialPieces(Map<String, Integer> initialStock, String workPiece, int quantity, Map<String, Boolean> visited) {
        if (visited.containsKey(workPiece)) {
            return; // Já visitado, não precisa acumular novamente
        }
        visited.put(workPiece, true);

        for (Transformation t : transformations) {
            if (t.producedPiece.equals(workPiece)) {
                if (t.startingPiece.equals("P1") || t.startingPiece.equals("P2")) {
                    initialStock.put(t.startingPiece, initialStock.getOrDefault(t.startingPiece, 0) + quantity);
                } else {
                    accumulateInitialPieces(initialStock, t.startingPiece, quantity, visited);
                }
            }
        }
    }

    public static void calculateTotalInitialPieces(List<Order> orders) {
        Map<String, Integer> totalInitialPieces = new HashMap<>();

        for (Order order : orders) {
            accumulateInitialPieces(totalInitialPieces, order.workPiece, Integer.parseInt(order.quantity), new HashMap<>());
        }

        System.out.println("Total initial pieces needed for each type:");
        for (Map.Entry<String, Integer> entry : totalInitialPieces.entrySet()) {
            System.out.println("Piece: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }
    }

    public static OrderResult getNextOrder(List<Order> orders, int currentDay, Map<String, Double> rawMaterialCosts) {
        if (orders.isEmpty()) {
            return null;
        }
        Map<String, Boolean> visited = new HashMap<>();
        Order order = orders.remove(0);
        int quantity = Integer.parseInt(order.quantity);
        double processingDays = calculateProcessingTimeInDays(order.workPiece, quantity, true);

        int dueDate = Integer.parseInt(order.dueDate);
        int startDate = dueDate - (int) Math.ceil(processingDays);

        // Verificar se a data de início é anterior ao dia 5
        if (startDate < 5) {
            startDate = 5;
        }

        // Verificar se a ordem pode começar no dia atual ou precisa esperar
        if (startDate < currentDay) {
            startDate = currentDay;
        }

        int endDate = startDate + (int) Math.ceil(processingDays); // Calcula o dia de término da ordem
        double rawMaterialCost = calculateRawMaterialCost(order.workPiece, quantity, rawMaterialCosts, visited , new HashMap<>());
        OrderResult orderResult = new OrderResult(order.orderId, order.workPiece, quantity, startDate, endDate, 0);

        // Calcula o custo unitário
        calculateUnitCost(orderResult, rawMaterialCost, startDate - 4, endDate);

        // Salva o estado atual no arquivo JSON
        saveOrderResult(orderResult);

        return orderResult;
    }

    public static Supplier getBestSupplier(String initialPiece, int quantity) {
        List<Supplier> suppliers = loadSuppliers();
        Supplier bestSupplier = null;
        double bestPrice = Double.MAX_VALUE;

        for (Supplier supplier : suppliers) {
            if (supplier.piece.equals(initialPiece) && supplier.minimumOrder <= quantity) {
                double totalPrice = supplier.pricePerPiece * quantity;
                if (totalPrice < bestPrice) {
                    bestPrice = totalPrice;
                    bestSupplier = supplier;
                }
            }
        }
        return bestSupplier;
    }

    public static void calculateUnitCost(OrderResult orderResult, double rawMaterialCost, int arrivalDate, int dispatchDate) {
        double rc = rawMaterialCost;
        double pt = (orderResult.endDate - orderResult.startDate) * 60; // Convertendo dias para segundos
        double pc = pt * 1; // Pc = €1 * Pt
        double dc = rc * (dispatchDate - arrivalDate) * 0.01; // Dc = Rc * (Dd - Ad) * 1%

        double tc = rc + pc + dc; // Tc = Rc + Pc + Dc

        orderResult.cost = tc;

        System.out.println("Order ID: " + orderResult.orderId + ", Raw Material Cost (Rc): " + rc + ", Production Cost (Pc): " + pc + ", Depreciation Cost (Dc): " + dc + ", Total Cost (Tc): " + tc);
    }

    public static double calculateRawMaterialCost(String workPiece, int quantity, Map<String, Double> rawMaterialCosts, Map<String, Boolean> visited, Map<String, SupplierUsage> supplierUsageMap) {
        double totalRawMaterialCost = 0;
        if (visited.containsKey(workPiece)) {
            return 0; // Já visitado, não precisa acumular novamente
        }
        visited.put(workPiece, true);

        for (Transformation t : transformations) {
            if (t.producedPiece.equals(workPiece)) {
                if (t.startingPiece.equals("P1") || t.startingPiece.equals("P2")) {
                    double pieceCost = rawMaterialCosts.get(t.startingPiece) * quantity;
                    totalRawMaterialCost += pieceCost;

                    Supplier bestSupplier = getBestSupplier(t.startingPiece, quantity);
                    if (bestSupplier != null) {
                        supplierUsageMap.put(t.startingPiece, new SupplierUsage(bestSupplier.name, t.startingPiece, quantity, bestSupplier.pricePerPiece));
                    }
                } else {
                    totalRawMaterialCost += calculateRawMaterialCost(t.startingPiece, quantity, rawMaterialCosts, visited, supplierUsageMap);
                }
            }
        }
        return totalRawMaterialCost;
    }

    public static void saveOrderResult(OrderResult orderResult) {
        try (FileWriter writer = new FileWriter(orderResult.orderId + ".json")) {
            writer.write(orderResult.toJSON().toString(4)); // Formatar com indentação de 4 espaços
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean processNextOrder(List<Order> orders, List<OrderResult> processedOrders, int[] currentDay, Map<String, Double> rawMaterialCosts) {
        OrderResult orderResult = getNextOrder(orders, currentDay[0], rawMaterialCosts);
        if (orderResult != null) {
            currentDay[0] = orderResult.endDate; // Atualiza o dia atual para quando a ordem termina
            processedOrders.add(orderResult);
            System.out.println("Processed Order: " + orderResult.toJSON());
            return true;
        } else {
            System.out.println("No more orders to process.");
            return false;
        }
    }

    public static void printOrderStatus(List<Order> allOrders, List<OrderResult> processedOrders) {
        System.out.println("Processed Orders:");
        for (OrderResult processedOrder : processedOrders) {
            System.out.println(processedOrder.toJSON());
        }

        System.out.println("Unprocessed Orders:");
        for (Order unprocessedOrder : allOrders) {
            System.out.println(unprocessedOrder.toString());
        }
    }

    public interface OrderListener {
        void onNewOrders(List<Order> orders);
    }
    
    public static void main(String[] args) {
        
    }
}
