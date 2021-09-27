package Matcher.util;

import Matcher.components.OrderBook.OrderSQL;

import java.util.Comparator;

public interface PriceTimeComparator extends Comparator<OrderSQL> {
    int compare(OrderSQL orderSQL1, OrderSQL orderSQL2);
}
