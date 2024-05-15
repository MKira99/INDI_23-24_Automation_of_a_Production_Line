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



}


