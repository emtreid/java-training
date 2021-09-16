package Matcher.components;

import org.junit.Assert;
import org.junit.Test;

public class TradeTest {
    String buyer = "buyer";
    String seller = "seller";
    double volume = 1;
    double price = 1.5;

    @Test
    public void createTrade() {
        Trade trade = new Trade(buyer, seller, volume, price);
    }

    @Test
    public void getTradeDetails() {
        Trade trade = new Trade(buyer, seller, volume, price);
        Assert.assertEquals(trade.getBuyer(), buyer);
        Assert.assertEquals(trade.getSeller(), seller);
        Assert.assertEquals(trade.getVolume(), volume, 1e-5);
        Assert.assertEquals(trade.getPrice(), price, 1e-5);
        Assert.assertNotNull(trade.getTimestamp());
        Assert.assertNotNull(trade.getId());
    }
}
