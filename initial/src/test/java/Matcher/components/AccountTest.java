package Matcher.components;

import Matcher.components.OrderBook.OrderBook;
import org.junit.Test;
import org.junit.Assert;

public class AccountTest {
    String username = "user";
    double startingGBP = 100;
    double startingBTC = 150;

    @Test
    public void initialiseAccount() {
        Account account = new Account(username, startingGBP, startingBTC);
    }

    @Test
    public void getAccountDetails() {
        Account account = new Account(username, startingGBP, startingBTC);
        Assert.assertEquals(account.getUsername(), username);
        Assert.assertEquals(account.getBalanceGBP(), startingGBP, 1e-5);
        Assert.assertEquals(account.getBalanceBTC(), startingBTC, 1e-5);
    }

    @Test
    public void addRemoveCurrency() {
        Account account = new Account(username, startingGBP, startingBTC);
        account.addGBP(50.5);
        Assert.assertEquals(account.getBalanceGBP(), startingGBP + 50.5, 1e-5);
        account.addBTC(50.5);
        Assert.assertEquals(account.getBalanceBTC(), startingBTC + 50.5, 1e-5);
        account.subtractGBP(50.5);
        Assert.assertEquals(account.getBalanceGBP(), startingGBP, 1e-5);
        account.subtractBTC(50.5);
        Assert.assertEquals(account.getBalanceBTC(), startingBTC, 1e-5);
    }

    @Test
    public void privateOrderBook() {
        Order order1 = new Order("user1", "Buy", 5, 1);
        Order order2 = new Order("user1", "Buy", 0, 2);
        Order order3 = new Order("user1", "Sell", 10, 1.5);
        Order order4 = new Order("user2", "Buy", 2, 3);
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        Account account = new Account("user1", startingGBP, startingBTC);
        account.updateOrderBook(orderBook);
        OrderBook privateOrderBook = account.getPrivateOrderBook();
        Assert.assertEquals(privateOrderBook.getBuy().size(), 2);
        Assert.assertEquals(privateOrderBook.getSell().size(), 1);
    }
}
