package Matcher.components;

import Matcher.components.Account.Account;
import Matcher.components.OrderBook.OrderBook;
import org.junit.Test;
import org.junit.Assert;

public class AccountTest {
    String username = "user";
    int startingGBP = 100;
    int startingBTC = 150;

    @Test
    public void initialiseAccount() {
        Account account = new Account(username, "password", 1, startingGBP, startingBTC);
    }

    @Test
    public void getAccountDetails() {
        Account account = new Account(username, "password", 1, startingGBP, startingBTC);
        Assert.assertEquals(account.getUsername(), username);
        Assert.assertEquals(account.getGbp(), startingGBP);
        Assert.assertEquals(account.getBtc(), startingBTC);
    }

    @Test
    public void addRemoveCurrency() {
        Account account = new Account(username, "password", 1, startingGBP, startingBTC);
        account.addGBP(50);
        Assert.assertEquals(account.getGbp(), startingGBP + 50);
        account.addBTC(50);
        Assert.assertEquals(account.getBtc(), startingBTC + 50);
        account.subtractGBP(50);
        Assert.assertEquals(account.getGbp(), startingGBP);
        account.subtractBTC(50);
        Assert.assertEquals(account.getBtc(), startingBTC);
    }

    @Test
    public void privateOrderBookDeprecated() {
        Order order1 = new Order("user1", "Buy", 5, 1);
        Order order2 = new Order("user1", "Buy", 0, 2);
        Order order3 = new Order("user1", "Sell", 10, 1);
        Order order4 = new Order("user2", "Buy", 2, 3);
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        Account account = new Account("user1", "password", 1, startingGBP, startingBTC);
    }
}
