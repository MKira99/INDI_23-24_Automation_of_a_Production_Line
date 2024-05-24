package Others;

import Others.ConsolidatedOrderSystem.*;  
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
}