package MES_Logic;

import java.util.ArrayList;
import java.util.List;


public  class OrderList {

    private static List<Order> listOrder = new ArrayList<>();

    public static List<Order> getListOrder()
    {

        return listOrder;

    }
    public static void addOrder(Order ordem)
    {

        listOrder.add(ordem);

    }
    public static void removeOrder(Order ordem)
    {

        listOrder.remove(ordem);

    }
    public static void sortlist()
    {

        listOrder.sort((o1, o2) -> Integer.compare(o1.getStartDay(), o2.getStartDay()));

    }
    public static void clearList()
    {

        listOrder.clear();

    }
    public static void printList()
    {

        for (Order order : listOrder) {
            System.out.println("Order ID: " + order.getOrderID() + " Piece Type: " + order.getPieceType() + " Quantity: " + order.getQuantity() + " Start Day: " + order.getStartDay() + " End Day: " + order.getEndDay());
        }

    }


}


