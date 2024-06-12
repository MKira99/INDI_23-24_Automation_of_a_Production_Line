package MES_Logic;

import java.util.ArrayList;
import java.util.List;


public class OrderList {

    private List<Order> listOrder = new ArrayList<>();
    private int size=0;

    public List<Order> getListOrder()
    {

        return listOrder;

    }
    public void addOrder(Order ordem)
    {

        listOrder.add(ordem);
        size=size+1;

    }
    public void removeOrder(Order ordem)
    {

        listOrder.remove(ordem);
        size=size-1;

    }
    public void sortlist()
    {

        listOrder.sort((o1, o2) -> Integer.compare(o1.getStartDay(), o2.getStartDay()));

    }
    public void clearList()
    {

        listOrder.clear();
        size=0;

    }
    public void printList()
    {

        for (Order order : listOrder) {
            System.out.println("Order ID: " + order.getOrderID() + " Piece Type: " + order.getPieceType() + " Quantity: " + order.getQuantity() + " Start Day: " + order.getStartDay() + " End Day: " + order.getEndDay());
        }

    }
    public int size()
    {
        return size;
    }


}


