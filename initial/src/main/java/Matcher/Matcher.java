package Matcher;

import Matcher.components.Account;
import Matcher.components.AggregatedOrderBook;
import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Trade;

import java.util.ArrayList;
import java.util.HashMap;

public class Matcher {
    private final HashMap<String, Account> accountList;
    private final ArrayList<Trade> tradeHistory;
    private OrderBook orderBook;
    private final AggregatedOrderBook aggregatedOrderBook;

    public Matcher() {
        accountList = new HashMap<String, Account>();
        tradeHistory = new ArrayList<Trade>();
        orderBook = new OrderBook();
        aggregatedOrderBook = new AggregatedOrderBook(orderBook);
    }

    public Account getAccount(String username) {
        return accountList.get(username);
    }

    public ArrayList<Trade> getTradeHistory() {
        return tradeHistory;
    }

    public AggregatedOrderBook getAggregatedOrderBook() {
        return aggregatedOrderBook;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void createAccount(String username, double startingGBP, double startingBTC) {
        Account newAccount = new Account(username, startingGBP, startingBTC);
        if (getAccount(username) == null)
            accountList.put(username, newAccount);
    }

    private void updatePrivateOrderBooks() {
        for (Account account : accountList.values()
        ) {
            account.updateOrderBook(orderBook);
        }
    }

    public void processOrder(Order order) {
        try {
            Account currentAccount = accountList.get(order.getUsername());
            if (currentAccount != null) {
                currentAccount.chargeAccount(order);
                Order remainingOrder = makeTrades(order);
                orderBook.addOrder(remainingOrder);
                orderBook.filterEmpty();
                orderBook.sortOrders();
                aggregatedOrderBook.updateBook(orderBook);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelOrder(String id) {
        OrderBook newOrderBook = new OrderBook();
        for (Order order : orderBook.getBuy()
        ) {
            if (!order.getId().equals(id)) {
                newOrderBook.addOrder(order);
            } else {
                Account currentAccount = getAccount(order.getUsername());
                currentAccount.refundOrder(order);
            }
        }
        for (Order order : orderBook.getSell()
        ) {
            if (!order.getId().equals(id)) {
                newOrderBook.addOrder(order);
            } else {
                Account currentAccount = getAccount(order.getUsername());
                currentAccount.refundOrder(order);
            }
        }
        orderBook = newOrderBook;
        orderBook.sortOrders();
        aggregatedOrderBook.updateBook(orderBook);
    }

    public void cancelAllOrders(String username) {
        updatePrivateOrderBooks();
        Account currentAccount = getAccount(username);
        for (Order order : currentAccount.getPrivateOrderBook().getBuy()
        ) {
            cancelOrder(order.getId());
        }
        for (Order order : currentAccount.getPrivateOrderBook().getSell()
        ) {
            cancelOrder(order.getId());
        }
        updatePrivateOrderBooks();
    }

    private void addTrade(Trade newTrade) {
        tradeHistory.add(newTrade);
    }

    private Order makeTrades(Order order) throws Exception {
        ArrayList<Order> oppositeOrderBook;
        if (order.getAction().equals("Buy")) {
            oppositeOrderBook = orderBook.getSell();
        } else if (order.getAction().equals("Sell")) {
            oppositeOrderBook = orderBook.getBuy();
        } else {
            throw new Exception("Action must be Buy or Sell");
        }
        for (Order previousOrder : oppositeOrderBook) {
            if (!order.getUsername().equals(previousOrder.getUsername())) {
                // Create and push trade, update volumes of new and previous orders, until one or both are exhausted
                Order buyOrder = order.getAction().equals("Buy") ? order : previousOrder;
                Order sellOrder = order.getAction().equals("Sell") ? order : previousOrder;
                Trade newTrade = new Trade(buyOrder, sellOrder);
                Account buyAccount = getAccount(newTrade.getBuyer());
                Account sellAccount = getAccount(newTrade.getSeller());
                buyAccount.creditAccount(newTrade, buyOrder.getPrice());
                sellAccount.creditAccount(newTrade, buyOrder.getPrice());
                addTrade(newTrade);
                order.setVolume(order.getVolume() - newTrade.getVolume());
                previousOrder.setVolume(previousOrder.getVolume() - newTrade.getVolume());
                if (order.getVolume() < 1e-5) {
                    break;
                }
            }
        }
        return order;
    }
}
