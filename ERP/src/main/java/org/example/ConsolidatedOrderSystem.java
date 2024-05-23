package org.example;
import java.util.*;

public class ConsolidatedOrderSystem {

    // Order class
    public static class Order {
        private String clientName;
        private int orderNumber;
        private String workpiece;
        private int quantity;
        private int dueDate;
        private int latePen;
        private int earlyPen;

        public Order(String clientName, int orderNumber, String workpiece, int quantity, int dueDate, int latePen, int earlyPen) {
            this.clientName = clientName;
            this.orderNumber = orderNumber;
            this.workpiece = workpiece;
            this.quantity = quantity;
            this.dueDate = dueDate;
            this.latePen = latePen;
            this.earlyPen = earlyPen;
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

        public int getLatePen() {
            return latePen;
        }

        public int getEarlyPen() {
            return earlyPen;
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
                    '}';
        }
    }

    // OrderInformation class
    public static class OrderInformation {
        private int piece1Quantity;
        private int piece2Quantity;

        public OrderInformation(int piece1Quantity, int piece2Quantity) {
            this.piece1Quantity = piece1Quantity;
            this.piece2Quantity = piece2Quantity;
        }

        public int getPiece1Quantity() {
            return piece1Quantity;
        }

        public void setPiece1Quantity(int piece1Quantity) {
            this.piece1Quantity = piece1Quantity;
        }

        public int getPiece2Quantity() {
            return piece2Quantity;
        }

        public void setPiece2Quantity(int piece2Quantity) {
            this.piece2Quantity = piece2Quantity;
        }
    }

    // OrderSystem class
    public static class OrderSystem {
        static ArrayList<Order> orders = new ArrayList<Order>();

        public static int addOrder(Order order) {
            orders.add(order);
            return orders.size();
        }

        public static Order getOrderByNumber(int number) {
            for (Order order : orders) {
                if (order.getOrderNumber() == number) {
                    return order;
                }
            }
            return null;
        }
    }

    // OrderToMES class
    public static class OrderToMES {
        private int field1;
        private int field2;
        private int field3;

        public int getField1() {
            return field1;
        }

        public void setField1(int field1) {
            this.field1 = field1;
        }

        public int getField2() {
            return field2;
        }

        public void setField2(int field2) {
            this.field2 = field2;
        }

        public int getField3() {
            return field3;
        }

        public void setField3(int field3) {
            this.field3 = field3;
        }

        @Override
        public String toString() {
            return "OrderToMES{" +
                    "field1=" + field1 +
                    ", field2=" + field2 +
                    ", field3=" + field3 +
                    '}';
        }
    }
    public static void main(String[] args){

    }

}