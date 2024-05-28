package Others;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabase {
        // Order class
    public static class OrderDb {
        private String clientName;
        private int orderNumber;
        private String workpiece;
        private int quantity;
        private int dueDate;
        private double latePen;
        private double earlyPen;
        private double orderCost;
        private int startDate;
        private int endDate;
        private boolean sendedMes;

        public OrderDb(String clientName, int orderNumber, String workpiece, int quantity, int dueDate, double latePen, double earlyPen, double orderCost, int startDate, int endDate, boolean sendedMes) {
            this.clientName = clientName;
            this.orderNumber = orderNumber;
            this.workpiece = workpiece;
            this.quantity = quantity;
            this.dueDate = dueDate;
            this.latePen = latePen;
            this.earlyPen = earlyPen;
            this.orderCost = orderCost;
            this.startDate = startDate;
            this.endDate = endDate;
            this.sendedMes = sendedMes;
        }

        public String getClientName() {
            return clientName;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public String getWorkpiece() {
            return workpiece;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getDueDate() {
            return dueDate;
        }

        public double getLatePen() {
            return latePen;
        }

        public double getEarlyPen() {
            return earlyPen;
        }

        public double getOrderCost() {
            return orderCost;
        }

        public int getStartDate() {
            return startDate;
        }

        public int getEndDate() {
            return endDate;
        }

        public boolean getSendedMes() {
            return sendedMes;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "clientName='" + clientName + '\'' +
                    ", orderNumber='" + orderNumber + '\'' +
                    ", workpiece='" + workpiece + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", dueDate='" + dueDate + '\'' +
                    ", latePen='" + latePen + '\'' +
                    ", earlyPen='" + earlyPen + '\'' +
                    ", orderCost='" + orderCost + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", sendedMes='" + sendedMes + '\'' +
                    '}';
        }
    }

    public static class OrderSystemDb {
        static ArrayList<OrderDb> orders = new ArrayList<OrderDb>();

        public static int addOrderDb(OrderDb orderDb) {
            orders.add(orderDb);
            return orders.size();
        }

        // Método para retornar todas as ordens
        public static List<OrderDb> getAllOrders() {
            return new ArrayList<>(orders); // Retorna uma cópia da lista para evitar modificações externas
        }

        public static OrderDb getOrderDbByNumber(int number) {
            for (OrderDb orderDb : orders) {
                if (orderDb.getOrderNumber() == number) {
                    return orderDb;
                }
            }
            return null;
        }
    }

}
