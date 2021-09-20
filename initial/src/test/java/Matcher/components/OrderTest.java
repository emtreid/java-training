package Matcher.components;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Test;
import org.junit.Assert;

import javax.validation.Valid;
import java.math.BigDecimal;


public class OrderTest {
    String username = "user";
    String action = "Buy";
    int volume = 2;
    int price = 52;

    @Test
    public void createOrder() {
        Order order = new Order(username, action, volume, price);
    }

    @Test
    public void getOrderDetails() {
        Order order = new Order(username, action, volume, price);
        Assert.assertEquals(order.getUsername(), username);
        Assert.assertEquals(order.getAction(), action);
        Assert.assertEquals(order.getVolume(), volume);
        Assert.assertEquals(order.getPrice(), price);
        Assert.assertNotNull(order.getTimestamp());
        Assert.assertNotNull(order.getId());
    }

    @Test
    public void setOrderVolume() {
        Order order = new Order(username, action, volume, price);
        order.setVolume(1);
        Assert.assertEquals(order.getVolume(), 1);
    }

    @Test
    public void validatePriceVolume() {
        Order noPriceOrder = new Order(username, action, volume, 0);
        System.out.println(noPriceOrder.getPrice());
        Assert.assertNotNull(noPriceOrder);
    }


}
