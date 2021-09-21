package Matcher;


import Matcher.components.Order;
import org.junit.Assert;
import org.junit.Test;

public class MatcherTest {
    Order order1 = new Order("Elliott", "Buy", 15, 25);
    Order order2 = new Order("Elliott", "Sell", 10, 20);
    Order order3 = new Order("Andrea", "Buy", 5, 15);
    Order order4 = new Order("Andrea", "Sell", 25, 20);


    @Test
    public void matcherInitialises() {
        Matcher matcher = new Matcher();
    }

    @Test
    public void addOrder() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", "password", 1000, 1500);
        matcher.processOrder("Elliottpassword".hashCode(), order1);
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 1);
        matcher.processOrder("Elliottpassword".hashCode(), order2);
        Assert.assertEquals(matcher.getOrderBook().getSell().size(), 1);
    }

    @Test
    public void makeTrade() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", 1000, 1500);
        matcher.createAccount("Andrea", 1000, 1500);
        matcher.processOrder("Elliottpassword".hashCode(), order1);
        matcher.processOrder("Andreapassword".hashCode(), order4);
        Assert.assertEquals(matcher.getTradeHistory().size(), 1);
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 0);
    }

    @Test
    public void cancelOrder() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", 1000, 1500);
        matcher.createAccount("Andrea", 1000, 1500);
        matcher.processOrder("Elliottpassword".hashCode(), order1);
        System.out.println(matcher.getOrderBook().getBuy().size());
        matcher.processOrder("Elliottpassword".hashCode(), order2);
        System.out.println(matcher.getOrderBook().getBuy().size());
        matcher.processOrder("Andreapassword".hashCode(), order3);
        System.out.println(matcher.getOrderBook().getBuy().size());
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 2);
        matcher.cancelOrder("Andreapassword".hashCode(), order3.getId(), false);
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 1);
    }

    @Test
    public void chargeAndRefund() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", 1000, 1500);
        matcher.processOrder("Elliottpassword".hashCode(), order1);
        Assert.assertEquals(matcher.getAccount("Elliott").getGbp(),
                1000 - (long) order1.getPrice() * order1.getVolume());
        matcher.cancelOrder("Elliottpassword".hashCode(), order1.getId(), false);
        Assert.assertEquals(matcher.getAccount("Elliott").getGbp(),
                1000);
    }

    @Test
    public void manyOrdersConsistentBalance() {
        Matcher matcher = new Matcher();
        String[] users = {"Andrea", "Bob", "Elliott"};
        int[] tokens = {"Andreapassword".hashCode(), "Bobpassword".hashCode(), "Elliottpassword".hashCode()};
        String[] actions = {"Buy", "Sell"};
        int[] volumes = {2, 3, 5, 4};
        int[] prices = {4, 3, 2, 1};
        for (String username : users
        ) {
            matcher.createAccount(username, 50000, 50000);
        }
        Assert.assertEquals(matcher.getAccount("Andrea").getGbp()
                + matcher.getAccount("Bob").getGbp()
                + matcher.getAccount("Elliott").getGbp(), 150000);
        for (int i = 0; i < 100; i++) {
            Order newOrder = new Order(users[i % 3], actions[i % 2], volumes[i % 4], prices[i % 4]);
            matcher.processOrder(tokens[i % 3], newOrder);
        }
        System.out.println(matcher.getTradeHistory().size());
        matcher.cancelAllOrders("Andreapassword".hashCode());
        matcher.cancelAllOrders("Bobpassword".hashCode());
        matcher.cancelAllOrders("Elliottpassword".hashCode());
        Assert.assertEquals(matcher.getAccount("Andrea").getGbp()
                + matcher.getAccount("Bob").getGbp()
                + matcher.getAccount("Elliott").getGbp(), 150000);
    }
}
