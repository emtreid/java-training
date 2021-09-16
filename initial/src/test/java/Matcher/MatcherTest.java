package Matcher;


import Matcher.components.Order;
import org.junit.Assert;
import org.junit.Test;

public class MatcherTest {
    Order order1 = new Order("Elliott", "Buy", 1.5, 2.5);
    Order order2 = new Order("Elliott", "Sell", 1.5, 2);
    Order order3 = new Order("Andrea", "Buy", 5, 1.5);
    Order order4 = new Order("Andrea", "Sell", 2.5, 2);


    @Test
    public void matcherInitialises() {
        Matcher matcher = new Matcher();
    }

    @Test
    public void addOrder() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", 100, 150);
        matcher.processOrder(order1);
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 1);
        matcher.processOrder(order2);
        Assert.assertEquals(matcher.getOrderBook().getSell().size(), 1);
    }

    @Test
    public void makeTrade() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", 100, 150);
        matcher.createAccount("Andrea", 100, 150);
        matcher.processOrder(order1);
        matcher.processOrder(order4);
        Assert.assertEquals(matcher.getTradeHistory().size(), 1);
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 0);
    }

    @Test
    public void cancelOrder() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", 100, 150);
        matcher.createAccount("Andrea", 100, 150);
        matcher.processOrder(order1);
        matcher.processOrder(order2);
        matcher.processOrder(order3);
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 2);
        matcher.cancelOrder(order3.getId());
        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 1);
    }

    @Test
    public void chargeAndRefund() {
        Matcher matcher = new Matcher();
        matcher.createAccount("Elliott", 100, 150);
        matcher.processOrder(order1);
        Assert.assertEquals(matcher.getAccount("Elliott").getBalanceGBP(),
                100 - order1.getPrice() * order1.getVolume(), 1e-5);
        matcher.cancelOrder(order1.getId());
        Assert.assertEquals(matcher.getAccount("Elliott").getBalanceGBP(),
                100, 1e-5);
    }

    @Test
    public void manyOrdersConsistentBalance() {
        Matcher matcher = new Matcher();
        String[] users = {"Andrea", "Bob", "Elliott"};
        String[] actions = {"Buy", "Sell"};
        double[] volumes = {2, 3, 5, 4};
        double[] prices = {4, 3, 2, 1};
        for (String username : users
        ) {
            matcher.createAccount(username, 5000, 5000);
        }
        Assert.assertEquals(matcher.getAccount("Andrea").getBalanceGBP()
                + matcher.getAccount("Bob").getBalanceGBP()
                + matcher.getAccount("Elliott").getBalanceGBP(), 15000, 1e-5);
        for (int i = 0; i < 100; i++) {
            Order newOrder = new Order(users[i % 3], actions[i % 2], volumes[i % 4], prices[i % 4]);
            matcher.processOrder(newOrder);
        }
        System.out.println(matcher.getTradeHistory().size());
        matcher.cancelAllOrders("Andrea");
        matcher.cancelAllOrders("Bob");
        matcher.cancelAllOrders("Elliott");
        Assert.assertEquals(matcher.getAccount("Andrea").getBalanceGBP()
                + matcher.getAccount("Bob").getBalanceGBP()
                + matcher.getAccount("Elliott").getBalanceGBP(), 15000, 1e-5);
    }
}
