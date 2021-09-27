package Matcher.util;

import Matcher.components.OrderBook.OrderSQL;

public class BuyComparator implements PriceTimeComparator {
    public int compare(OrderSQL orderSQL1, OrderSQL orderSQL2) {
        if (orderSQL1.getPrice() < orderSQL2.getPrice()) {
            return 1;
        } else if (orderSQL1.getPrice() > orderSQL2.getPrice()) {
            return -1;
        } else {
            return orderSQL1.getTimestamp().compareTo(orderSQL2.getTimestamp());
        }
    }
}
