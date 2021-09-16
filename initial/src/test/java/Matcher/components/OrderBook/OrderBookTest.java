package Matcher.components.OrderBook;

import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class OrderBookTest {
    Order order1 = new Order("user1", "Buy", 5, 1);
    Order order2 = new Order("user1", "Buy", 0, 2);
    Order order3 = new Order("user1", "Sell", 10, 4.5);
    Order order4 = new Order("user2", "Buy", 2, 3);
    Order order5 = new Order("user2", "Sell", 2, 4);
    Order order6 = new Order("user3", "Sell", 2, 5);

    @Test
    public void createOrderBook() {
        OrderBook orderBook = new OrderBook();
    }

    @Test
    public void populateOrderBook() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        ArrayList<Order> buyList = orderBook.getBuy();
        ArrayList<Order> sellList = orderBook.getSell();
        Assert.assertEquals(buyList.size(), 3);
        Assert.assertEquals(sellList.size(), 1);
    }

    @Test
    public void removeEmptyOrders() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        orderBook.filterEmpty();
        ArrayList<Order> buyList = orderBook.getBuy();
        Assert.assertEquals(buyList.size(), 2);
    }

    @Test
    public void sortOrders() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        orderBook.addOrder(order5);
        orderBook.addOrder(order6);
        orderBook.sortOrders();
        Assert.assertEquals(orderBook.getBuy().get(0).getPrice(), 3, 1e-5);
        Assert.assertEquals(orderBook.getSell().get(0).getPrice(), 4, 1e-5);
    }
}
