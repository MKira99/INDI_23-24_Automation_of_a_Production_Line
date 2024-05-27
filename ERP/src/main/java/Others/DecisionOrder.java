package Others;

import Others.ConsolidatedOrderSystem.*;
import Others.OrderDatabase.OrderDb;

import java.util.List;

public class DecisionOrder {

    public static Order decideOrder(List<List<Order>> orderMatrix) {
        Order selectedOrder = null;

        for (List<Order> orderList : orderMatrix) {
            for (Order order : orderList) {
                if (selectedOrder == null || order.getDueDate() < selectedOrder.getDueDate()) {
                    selectedOrder = order;
                }
            }
        }

        return selectedOrder;
    }

    public static OrderDb decideOrderDb(List<List<OrderDb>> orderDbMatrix) {
        OrderDb selectedOrder = null;

        for (List<OrderDb> orderList : orderDbMatrix) {
            for (OrderDb order : orderList) {
                if (selectedOrder == null || order.getDueDate() < selectedOrder.getDueDate()) {
                    selectedOrder = order;
                }
            }
        }

        return selectedOrder;
    }
}