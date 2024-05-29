package DataBase;

import org.json.JSONObject;

public class OrderDatabase {
        // Order class
    public static class OrderDb {
        String orderId;
        String pieceType;
        String quantity;
        String startDay;
        String endDay;
        Boolean processed_1;
        Boolean processed_2;
        Boolean processed_3;
        Boolean processed_4;
        Boolean processed_5;

        public OrderDb(String orderId, String pieceType, String quantity, String startDay, String endDay, Boolean processed_1, Boolean processed_2, Boolean processed_3, Boolean processed_4, Boolean processed_5) {
            this.orderId = orderId;
            this.pieceType = pieceType;
            this.quantity = quantity;
            this.startDay = startDay;
            this.endDay = endDay;
            this.processed_1 = processed_1;
            this.processed_2 = processed_2;
            this.processed_3 = processed_3;
            this.processed_4 = processed_4;
            this.processed_5 = processed_5;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getPieceType() {
            return pieceType;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getStartDay() {
            return startDay;
        }

        public String getEndDay() {
            return endDay;
        }

        public Boolean getProcessed_1() {
            return processed_1;
        }

        public Boolean getProcessed_2() {
            return processed_2;
        }

        public Boolean getProcessed_3() {
            return processed_3;
        }

        public Boolean getProcessed_4() {
            return processed_4;
        }

        public Boolean getProcessed_5() {
            return processed_5;
        }


        public JSONObject toJSONDb() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", orderId);
            jsonObject.put("pieceType", pieceType);
            jsonObject.put("quantity", quantity);
            jsonObject.put("startDay", startDay);
            jsonObject.put("endDay", endDay);

            return jsonObject;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderId='" + orderId + '\'' +
                    ", pieceType='" + pieceType + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", startDay='" + startDay + '\'' +
                    ", endDay='" + endDay + '\'' +
                    ", processed_1='" + processed_1 + '\'' +
                    ", processed_2='" + processed_2 + '\'' +
                    ", processed_3='" + processed_3 + '\'' +
                    ", processed_4='" + processed_4 + '\'' +
                    ", processed_5='" + processed_5 + '\'' +
                    '}';
        }
    }

    public static class OrderSystemDb {
        static OrderDb orderActive;

        public static void addOrderDb(OrderDb orderDb) {
            //orders.add(orderDb);
            orderActive=orderDb;
        }

        // Método para retornar a ordem da database
        public static OrderDb getOrder() {
            return orderActive; // Retorna uma cópia da lista para evitar modificações externas
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
