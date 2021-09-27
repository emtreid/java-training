package Matcher.components;

import Matcher.components.OrderBook.OrderSQL;
import Matcher.components.OrderBook.OrderService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class AggregatedOrderBook {
    private final TreeMap<Long, Long> Buy;
    private final TreeMap<Long, Long> Sell;

    public AggregatedOrderBook(OrderService orderService) {
        Buy = populateBook(orderService.getOrderByAction("Buy"));
        Sell = populateBook(orderService.getOrderByAction("Sell"));
    }

    @JsonProperty("Buy")
    public TreeMap<Long, Long> getBuy() {
        return Buy;
    }

    @JsonProperty("Sell")
    public TreeMap<Long, Long> getSell() {
        return Sell;
    }

    private TreeMap<Long, Long> populateBook(List<OrderSQL> orderSQLList) {
        TreeMap<Long, Long> newMap = new TreeMap<Long, Long>();
        ArrayList<Long> prices = new ArrayList<Long>();
        ArrayList<Long> volumes = new ArrayList<Long>();
        for (OrderSQL orderSQL : orderSQLList
        ) {
            int priceIndex = prices.indexOf(orderSQL.getPrice());
            if (priceIndex == -1) {
                prices.add(orderSQL.getPrice());
                volumes.add(orderSQL.getVolume());
            } else {
                volumes.set(priceIndex, volumes.get(priceIndex) + orderSQL.getVolume());
            }
        }
        for (int i = 0; i < prices.size(); i++) {
            if (volumes.get(i) > 0)
                newMap.put(prices.get(i), volumes.get(i));
        }
        return newMap;
    }
}
