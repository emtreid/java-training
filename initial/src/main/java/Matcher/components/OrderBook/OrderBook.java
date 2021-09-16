package Matcher.components.OrderBook;

import Matcher.components.Order;
import Matcher.util.BuyComparator;
import Matcher.util.SellComparator;

import java.util.ArrayList;
import java.util.Collections;

public class OrderBook {
    private ArrayList<Order> buy;
    private ArrayList<Order> sell;

    public OrderBook() {
        buy = new ArrayList<Order>();
        sell = new ArrayList<Order>();
    }

    public ArrayList<Order> getBuy() {
        return buy;
    }

    public ArrayList<Order> getSell() {
        return sell;
    }

    public void emptyBook() {
        buy = new ArrayList<Order>();
        sell = new ArrayList<Order>();
    }

    public void addOrder(Order newOrder) {
        if (newOrder.getAction().equals("Buy")) {
            buy.add(newOrder);
        } else if (newOrder.getAction().equals("Sell")) {
            sell.add(newOrder);
        }
    }

    public void sortOrders() {
        buy.sort(new BuyComparator());
        sell.sort(new SellComparator());
    }

    public void filterEmpty() {
        ArrayList<Order> newBuy = new ArrayList<Order>();
        ArrayList<Order> newSell = new ArrayList<Order>();
        for (Order order : buy
        ) {
            if (order.getVolume() > 1e-5) {
                newBuy.add(order);
            }
        }
        for (Order order : sell
        ) {
            if (order.getVolume() > 1e-5) {
                newSell.add(order);
            }
        }
        buy = newBuy;
        sell = newSell;
    }
}
