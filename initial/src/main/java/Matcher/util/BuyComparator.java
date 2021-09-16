package Matcher.util;

import Matcher.components.Order;

public class BuyComparator implements PriceTimeComparator {
    public int compare(Order order1, Order order2) {
        if (order1.getPrice() < order2.getPrice()) {
            return 1;
        } else if (order1.getPrice() > order2.getPrice()) {
            return -1;
        } else {
            return order1.getTimestamp().compareTo(order2.getTimestamp());
        }
    }
}
