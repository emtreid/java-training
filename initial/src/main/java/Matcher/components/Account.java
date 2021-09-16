package Matcher.components;

import Matcher.components.OrderBook.OrderBook;

import javax.validation.constraints.Min;

public class Account {
    private final String username;
    @Min(0)
    private int balanceGBP;// balance*100
    @Min(0)
    private int balanceBTC;// balance*10
    private OrderBook privateOrderBook = new OrderBook();

    public Account(String username, int startingGBP, int startingBTC) {
        this.username = username;
        balanceGBP = startingGBP;
        balanceBTC = startingBTC;
    }

    public String getUsername() {
        return username;
    }

    public int getBalanceGBP() {
        return balanceGBP;
    }

    public int getBalanceBTC() {
        return balanceBTC;
    }

    public OrderBook getPrivateOrderBook() {
        return privateOrderBook;
    }

    public void addGBP(double amount) {
        balanceGBP += amount;
    }

    public void addBTC(double amount) {
        balanceBTC += amount;
    }

    public void subtractGBP(double amount) {
        balanceGBP -= amount;
    }

    public void subtractBTC(double amount) {
        balanceBTC -= amount;
    }

    public void chargeAccount(Order order) {
        if (order.getAction().equals("Buy")) {
            int cost = order.getPrice() * order.getVolume();
            subtractGBP(cost);
        } else if (order.getAction().equals("Sell")) {
            subtractBTC(order.getVolume());
        }
    }

    public void creditAccount(Trade trade, int buyOrderPrice) {
        if (trade.getBuyer().equals(username)) {
            int refund = trade.getVolume() * (buyOrderPrice - trade.getPrice());
            addGBP(refund);
            addBTC(trade.getVolume());
        }
        if (trade.getSeller().equals(username)) {
            int profit = trade.getPrice() * trade.getVolume();
            addGBP(profit);
        }
    }

    public void refundOrder(Order order) {
        if (order.getAction().equals("Buy")) {
            int cost = order.getPrice() * order.getVolume();
            addGBP(cost);
        } else if (order.getAction().equals("Sell")) {
            addBTC(order.getVolume());
        }
    }

    public void updateOrderBook(OrderBook mainOrderBook) {
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
