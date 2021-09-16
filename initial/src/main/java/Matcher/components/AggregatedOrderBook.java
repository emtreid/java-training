package Matcher.components;

import Matcher.components.OrderBook.OrderBook;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class AggregatedOrderBook {
    private TreeMap<Integer, Integer> buy;
    private TreeMap<Integer, Integer> sell;

    public AggregatedOrderBook(OrderBook orderBook) {
        buy = populateBook(orderBook.getBuy());
        sell = populateBook(orderBook.getSell());
    }

    public TreeMap<Integer, Integer> getBuy() {
        return buy;
    }

    public TreeMap<Integer, Integer> getSell() {
        return sell;
    }

    public void updateBook(OrderBook orderBook) {
        buy = populateBook(orderBook.getBuy());
        sell = populateBook(orderBook.getSell());
    }

    private TreeMap<Integer, Integer> populateBook(ArrayList<Order> orderList) {
        TreeMap<Integer, Integer> newMap = new TreeMap<Integer, Integer>();
        ArrayList<Integer> prices = new ArrayList<Integer>();
        ArrayList<Integer> volumes = new ArrayList<Integer>();
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
