package Matcher.util;

import Matcher.components.Order;

import java.util.Comparator;

public interface PriceTimeComparator extends Comparator<Order> {
    int compare(Order order1, Order order2);
}
