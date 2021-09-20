package Matcher.components.Account;

import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Trade;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Account {
    @NotNull
    @Size(min = 1)
    private final String username;
    @NotNull
    @Min(0)
    private long balanceGBP;// balance*100
    @NotNull
    @Min(0)
    private long balanceBTC;// balance*10
    private OrderBook privateOrderBook = new OrderBook();

    public Account(String username, long startingGBP, long startingBTC) {
        this.username = username;
        balanceGBP = startingGBP;
        balanceBTC = startingBTC;
    }

    public String getUsername() {
        return username;
    }

    public long getBalanceGBP() {
        return balanceGBP;
    }

    public long getBalanceBTC() {
        return balanceBTC;
    }

    public OrderBook getPrivateOrderBook() {
        return privateOrderBook;
    }

    public void addGBP(long amount) {
        balanceGBP += amount;
    }

    public void addBTC(long amount) {
        balanceBTC += amount;
    }

    public void subtractGBP(long amount) {
        balanceGBP -= amount;
    }

    public void subtractBTC(long amount) {
        balanceBTC -= amount;
    }

    public void chargeAccount(Order order) {
        if (order.getAction().equals("Buy")) {
            long cost = order.getPrice() * order.getVolume();
            subtractGBP(cost);
        } else if (order.getAction().equals("Sell")) {
            subtractBTC(order.getVolume());
        }
    }

    private void resetOrderBook() {
        privateOrderBook = new OrderBook();
    }

    public void creditAccount(Trade trade, long buyOrderPrice) {
        if (trade.getBuyer().equals(username)) {
            long refund = trade.getVolume() * (buyOrderPrice - trade.getPrice());
            addGBP(refund);
            addBTC(trade.getVolume());
        }
        if (trade.getSeller().equals(username)) {
            long profit = trade.getPrice() * trade.getVolume();
            addGBP(profit);
        }
    }

    public void refundOrder(Order order) {
        if (order.getAction().equals("Buy")) {
            long cost = order.getPrice() * order.getVolume();
            addGBP(cost);
        } else if (order.getAction().equals("Sell")) {
            addBTC(order.getVolume());
        }
    }

    public void updateOrderBook(OrderBook mainOrderBook) {
        resetOrderBook();
        for (Order order : mainOrderBook.getBuy()
        ) {
            if (order.getUsername().equals(username)) {
                privateOrderBook.addOrder(order);
            }
        }
        for (Order order : mainOrderBook.getSell()
        ) {
            if (order.getUsername().equals(username)) {
                privateOrderBook.addOrder(order);
            }
        }
    }


}
