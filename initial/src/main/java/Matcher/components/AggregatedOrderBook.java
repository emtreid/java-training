package Matcher.components;

import Matcher.components.OrderBook.OrderBook;

import java.util.ArrayList;
import java.util.TreeMap;

public class AggregatedOrderBook {
    private TreeMap<Long, Long> Buy;
    private TreeMap<Long, Long> Sell;

    public AggregatedOrderBook(OrderBook orderBook) {
        Buy = populateBook(orderBook.getBuy());
        Sell = populateBook(orderBook.getSell());
    }

    public TreeMap<Long, Long> getBuy() {
        return Buy;
    }

    public TreeMap<Long, Long> getSell() {
        return Sell;
    }

    public void updateBook(OrderBook orderBook) {
        Buy = populateBook(orderBook.getBuy());
        Sell = populateBook(orderBook.getSell());
    }

    private TreeMap<Long, Long> populateBook(ArrayList<Order> orderList) {
        TreeMap<Long, Long> newMap = new TreeMap<Long, Long>();
        ArrayList<Long> prices = new ArrayList<Long>();
        ArrayList<Long> volumes = new ArrayList<Long>();
        for (Order order : orderList
        ) {
            int priceIndex = prices.indexOf(order.getPrice());
            if (priceIndex == -1) {
                prices.add(order.getPrice());
                volumes.add(order.getVolume());
            } else {
                volumes.set(priceIndex, volumes.get(priceIndex) + order.getVolume());
            }
        }
        for (int i = 0; i < prices.size(); i++) {
            if (volumes.get(i) > 0)
                newMap.put(prices.get(i), volumes.get(i));
        }
        return newMap;
    }
}
