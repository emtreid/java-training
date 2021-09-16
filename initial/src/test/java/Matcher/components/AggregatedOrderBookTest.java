package Matcher.components;

import Matcher.components.OrderBook.OrderBook;
import org.junit.Assert;
import org.junit.Test;

public class AggregatedOrderBookTest {
    Order order1 = new Order("user1", "Buy", 5, 1);
    Order order2 = new Order("user1", "Buy", 0, 2);
    Order order3 = new Order("user1", "Sell", 10, 4);
    Order order4 = new Order("user2", "Buy", 2, 3);
    Order order5 = new Order("user2", "Sell", 2, 4);
    Order order6 = new Order("user3", "Sell", 2, 5);

    @Test
    public void initialiseAOB() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        orderBook.addOrder(order5);
        orderBook.addOrder(order6);
        AggregatedOrderBook aggregatedOrderBook = new AggregatedOrderBook(orderBook);
        Assert.assertEquals(aggregatedOrderBook.getSell().get(order3.getPrice()), order3.getVolume() + order5.getVolume(), 1e-5);
    }
}
