package Matcher;

import Matcher.components.Account.Account;
import Matcher.components.Account.Passwords;
import Matcher.components.AggregatedOrderBook;
import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class Matcher {
    private final HashMap<String, Account> accountList;
    private final ArrayList<Trade> tradeHistory;
    private OrderBook orderBook;
    private final AggregatedOrderBook aggregatedOrderBook;
    private final Passwords passwords;

    @Autowired
    public Matcher(Passwords passwords) {
        accountList = new HashMap<String, Account>();
        tradeHistory = new ArrayList<Trade>();
        orderBook = new OrderBook();
        aggregatedOrderBook = new AggregatedOrderBook(orderBook);
        this.passwords = passwords;
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

    public void createAccount(String username, String password, long startingGBP, long startingBTC) {
        Account newAccount = new Account(username, startingGBP, startingBTC);
        if (getAccount(username) == null) {
            accountList.put(username, newAccount);
            passwords.addAccount(username, password);
        }
        System.out.println(accountList.size());
    }

    public void createAccount(String username, long startingGBP, long startingBTC) {
        createAccount(username, "password", startingGBP, startingBTC);
    }

    private void updatePrivateOrderBooks() {
        System.out.println(orderBook.toString());
        for (Account account : accountList.values()
        ) {
            account.updateOrderBook(orderBook);
        }
    }

    public void processOrder(int token, Order order) {
        try {
            Account currentAccount = accountList.get(order.getUsername());
            if (order.getUsername().equals(passwords.authenticateToken(token))) {
                currentAccount.chargeAccount(order);
                Order remainingOrder = makeTrades(order);
                orderBook.addOrder(remainingOrder);
                orderBook.filterEmpty();
                orderBook.sortOrders();
                aggregatedOrderBook.updateBook(orderBook);
                updatePrivateOrderBooks();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelOrder(String id, int token, boolean cancelAll) {
        try {
            OrderBook newOrderBook = new OrderBook();
            String username = passwords.authenticateToken(token);
            String orderUsername = "";
            for (Order order : orderBook.getBuy()
            ) {
                if (!order.getId().equals(id)) {
                    newOrderBook.addOrder(order);
                } else {
                    orderUsername = order.getUsername();
                    Account currentAccount = getAccount(username);
                    if (orderUsername.equals(username))
                        currentAccount.refundOrder(order);
                }
            }
            for (Order order : orderBook.getSell()
            ) {
                if (!order.getId().equals(id)) {
                    newOrderBook.addOrder(order);
                } else {
                    orderUsername = order.getUsername();
                    Account currentAccount = getAccount(username);
                    if (orderUsername.equals(username))
                        currentAccount.refundOrder(order);
                }
            }
            if (orderUsername.equals(username)) {
                orderBook = newOrderBook;
                orderBook.sortOrders();
                aggregatedOrderBook.updateBook(orderBook);
                if (!cancelAll) updatePrivateOrderBooks();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelAllOrders(int token) {
        try {
            String username = passwords.authenticateToken(token);
            updatePrivateOrderBooks();
            Account currentAccount = getAccount(username);
            for (Order order : currentAccount.getPrivateOrderBook().getBuy()
            ) {
                cancelOrder(order.getId(), token, true);
            }
            for (Order order : currentAccount.getPrivateOrderBook().getSell()
            ) {
                cancelOrder(order.getId(), token, true);
            }
            updatePrivateOrderBooks();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
                if (buyOrder.getPrice() >= sellOrder.getPrice()) {
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
        }
        return order;
    }
}
