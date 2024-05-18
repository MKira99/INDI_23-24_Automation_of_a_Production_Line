package org.example;

import org.example.Order;

import java.util.ArrayList;

public class OrderSystem {
    static ArrayList<Order> orders = new ArrayList<Order>();

    public static int addOrder(Order order){
        orders.add(order);
        return orders.size();
    }

    public Order getOrderByNumber(int number) {
        for (Order order : orders) {
            if (order.getOrderNumber() == number) {
                return order;
            }
        }
        return null;
    }
}
