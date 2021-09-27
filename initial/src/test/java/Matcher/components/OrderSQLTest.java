package Matcher.components;

import Matcher.components.OrderBook.OrderSQL;
import org.junit.Test;
import org.junit.Assert;


public class OrderSQLTest {
    String username = "user";
    String action = "Buy";
    int volume = 2;
    int price = 52;

    @Test
    public void createOrder() {
        OrderSQL orderSQL = new OrderSQL(username, action, volume, price);
    }

    @Test
    public void getOrderDetails() {
        OrderSQL orderSQL = new OrderSQL(username, action, volume, price);
        Assert.assertEquals(orderSQL.getUsername(), username);
        Assert.assertEquals(orderSQL.getAction(), action);
        Assert.assertEquals(orderSQL.getVolume(), volume);
        Assert.assertEquals(orderSQL.getPrice(), price);
        Assert.assertNotNull(orderSQL.getTimestamp());
        Assert.assertNotNull(orderSQL.getId());
    }

    @Test
    public void setOrderVolume() {
        OrderSQL orderSQL = new OrderSQL(username, action, volume, price);
        orderSQL.setVolume(1);
        Assert.assertEquals(orderSQL.getVolume(), 1);
    }

    @Test
    public void validatePriceVolume() {
        OrderSQL noPriceOrderSQL = new OrderSQL(username, action, volume, 0);
        System.out.println(noPriceOrderSQL.getPrice());
        Assert.assertNotNull(noPriceOrderSQL);
    }


}
