package Matcher.components.OrderBook;

import Matcher.components.Order;
import Matcher.util.BuyComparator;
import Matcher.util.SellComparator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class OrderBook {
    private ArrayList<Order> Buy;
    private ArrayList<Order> Sell;

    public OrderBook() {
        Buy = new ArrayList<Order>();
        Sell = new ArrayList<Order>();
    }

    @JsonProperty("Buy")
    public ArrayList<Order> getBuy() {
        return Buy;
    }

    @JsonProperty("Sell")
    public ArrayList<Order> getSell() {
        return Sell;
    }

    public void emptyBook() {
        Buy = new ArrayList<Order>();
        Sell = new ArrayList<Order>();
    }

    public void addOrder(Order newOrder) {
        if (newOrder.getAction().equals("Buy")) {
            Buy.add(newOrder);
        } else if (newOrder.getAction().equals("Sell")) {
            Sell.add(newOrder);
        }
    }

    public void sortOrders() {
        Buy.sort(new BuyComparator());
        Sell.sort(new SellComparator());
    }

    public void filterEmpty() {
        ArrayList<Order> newBuy = new ArrayList<Order>();
        ArrayList<Order> newSell = new ArrayList<Order>();
        for (Order order : Buy
        ) {
            if (order.getVolume() > 1e-5) {
                newBuy.add(order);
            }
        }
        for (Order order : Sell
        ) {
            if (order.getVolume() > 1e-5) {
                newSell.add(order);
            }
        }
        Buy = newBuy;
        Sell = newSell;
    }
}
