package MES_Logic;

import java.util.ArrayList;
import java.util.List;


public  class OrderList {

    private static List<Order> listOrder = new ArrayList<>();
    private static int size=0;

    public static List<Order> getListOrder()
    {

        return listOrder;

    }
    public static void addOrder(Order ordem)
    {

        listOrder.add(ordem);
        size=size+1;

    }
    public static void removeOrder(Order ordem)
    {

        listOrder.remove(ordem);
        size=size-1;

    }
    public static void sortlist()
    {

        listOrder.sort((o1, o2) -> Integer.compare(o1.getStartDay(), o2.getStartDay()));

    }
    public static void clearList()
    {

        listOrder.clear();
        size=0;

    }
    public static void printList()
    {

        for (Order order : listOrder) {
            System.out.println("Order ID: " + order.getOrderID() + " Piece Type: " + order.getPieceType() + " Quantity: " + order.getQuantity() + " Start Day: " + order.getStartDay() + " End Day: " + order.getEndDay());
        }

    }
    public static int size()
    {
        return size;
    }


}


