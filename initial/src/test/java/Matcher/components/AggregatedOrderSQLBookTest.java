package Matcher.components;

import Matcher.components.OrderBook.OrderSQL;
import Matcher.components.OrderBook.OrderBook;
import org.junit.Assert;
import org.junit.Test;

public class AggregatedOrderSQLBookTest {
    OrderSQL orderSQL1 = new OrderSQL("user1", "Buy", 5, 1);
    OrderSQL orderSQL2 = new OrderSQL("user1", "Buy", 0, 2);
    OrderSQL orderSQL3 = new OrderSQL("user1", "Sell", 10, 4);
    OrderSQL orderSQL4 = new OrderSQL("user2", "Buy", 2, 3);
    OrderSQL orderSQL5 = new OrderSQL("user2", "Sell", 2, 4);
    OrderSQL orderSQL6 = new OrderSQL("user3", "Sell", 2, 5);

    @Test
    public void initialiseAOB() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(orderSQL1);
        orderBook.addOrder(orderSQL2);
        orderBook.addOrder(orderSQL3);
        orderBook.addOrder(orderSQL4);
        orderBook.addOrder(orderSQL5);
        orderBook.addOrder(orderSQL6);
        AggregatedOrderBook aggregatedOrderBook = new AggregatedOrderBook(orderBook);
        Assert.assertNotNull(aggregatedOrderBook.getSell().get(orderSQL3.getPrice()));

        Assert.assertEquals((long) aggregatedOrderBook.getSell().get(orderSQL3.getPrice()), orderSQL3.getVolume() + orderSQL5.getVolume());
    }
}
