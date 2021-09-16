package Matcher.components;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Test;
import org.junit.Assert;

import java.math.BigDecimal;


public class OrderTest {
    String username = "user";
    String action = "Buy";
    double volume = 1;
    double price = 5.2;

    @Test
    public void createOrder() {
        Order order = new Order(username, action, volume, price);
    }

    @Test
    public void getOrderDetails() {
        Order order = new Order(username, action, volume, price);
        Assert.assertEquals(order.getUsername(), username);
        Assert.assertEquals(order.getAction(), action);
        Assert.assertEquals(order.getVolume(), volume, 1e-5);
        Assert.assertEquals(order.getPrice(), price, 1e-5);
        Assert.assertNotNull(order.getTimestamp());
        Assert.assertNotNull(order.getId());
    }

    @Test
    public void setOrderVolume() {
        Order order = new Order(username, action, volume, price);
        order.setVolume(1);
        Assert.assertEquals(order.getVolume(), 1, 1e-5);
    }
}
