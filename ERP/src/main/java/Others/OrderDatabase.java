
package Others;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class OrderDatabase {
        // Order class
    public static class OrderDb {
        String clientName;
        String orderNumber;
        String workpiece;
        String quantity;
        String dueDate;
        String latePen;
        String earlyPen;
        String orderCost;
        String startDate;
        String endDate;
        Boolean sendedMes;
        String orderId;

        public OrderDb(String clientName, String orderNumber, String workpiece, String quantity, String dueDate, String latePen, String earlyPen, String orderCost, String startDate, String endDate, Boolean sendedMes) {
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
            this.orderId = clientName.replaceAll("\\s+", "") + "_" + orderNumber;
        }

        public String getClientName() {
            return clientName;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public String getWorkpiece() {
            return workpiece;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getDueDate() {
            return dueDate;
        }

        public String getLatePen() {
            return latePen;
        }

        public String getEarlyPen() {
            return earlyPen;
        }

        public String getOrderCost() {
            return orderCost;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public Boolean getSendedMes() {
            return sendedMes;
        }

        public JSONObject toJSONDb() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", orderId);
            jsonObject.put("workPiece", workpiece);
            jsonObject.put("quantity", quantity);
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("cost", orderCost);

            return jsonObject;
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
                    ", orderId='" + orderId + '\'' +
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

        /*public static OrderDb getOrderDbByNumber(int number) {
            for (OrderDb orderDb : orders) {
                if (orderDb.getOrderNumber() == number) {
                    return orderDb;
                }
            }
            return null;
        }*/
    }

}
