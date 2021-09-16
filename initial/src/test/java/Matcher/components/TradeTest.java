package Matcher.components;

import org.junit.Assert;
import org.junit.Test;

public class TradeTest {
    String buyer = "buyer";
    String seller = "seller";
    int volume = 1;
    int price = 2;

    @Test
    public void createTrade() {
        Trade trade = new Trade(buyer, seller, volume, price);
    }

    @Test
    public void getTradeDetails() {
        Trade trade = new Trade(buyer, seller, volume, price);
        Assert.assertEquals(trade.getBuyer(), buyer);
        Assert.assertEquals(trade.getSeller(), seller);
        Assert.assertEquals(trade.getVolume(), volume);
        Assert.assertEquals(trade.getPrice(), price);
        Assert.assertNotNull(trade.getTimestamp());
        Assert.assertNotNull(trade.getId());
    }
}
