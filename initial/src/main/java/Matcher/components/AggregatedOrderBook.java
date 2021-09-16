package Matcher.components;

import Matcher.components.OrderBook.OrderBook;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class AggregatedOrderBook {
    private TreeMap<Double, Double> buy;
    private TreeMap<Double, Double> sell;

    public AggregatedOrderBook(OrderBook orderBook) {
        buy = populateBook(orderBook.getBuy());
        sell = populateBook(orderBook.getSell());
    }

    public TreeMap<Double, Double> getBuy() {
        return buy;
    }

    public TreeMap<Double, Double> getSell() {
        return sell;
    }

    public void updateBook(OrderBook orderBook) {
        buy = populateBook(orderBook.getBuy());
        sell = populateBook(orderBook.getSell());
    }

    private TreeMap<Double, Double> populateBook(ArrayList<Order> orderList) {
        TreeMap<Double, Double> newMap = new TreeMap<Double, Double>();
        ArrayList<BigDecimal> prices = new ArrayList<BigDecimal>();
        ArrayList<Double> volumes = new ArrayList<Double>();
        for (Order order : orderList
        ) {
            BigDecimal decPrice = BigDecimal.valueOf(order.getPrice());
            BigDecimal roundedPrice = decPrice.setScale(2, RoundingMode.HALF_UP);
            int priceIndex = prices.indexOf(roundedPrice);
            if (priceIndex == -1) {
                prices.add(roundedPrice);
                volumes.add(order.getVolume());
            } else {
                volumes.set(priceIndex, volumes.get(priceIndex) + order.getVolume());
            }
        }
        for (int i = 0; i < prices.size(); i++) {
            if (volumes.get(i) > 1e-5)
                newMap.put(prices.get(i).doubleValue(), volumes.get(i));
        }
        return newMap;
    }
}
