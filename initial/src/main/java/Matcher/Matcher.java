package Matcher;

import Matcher.components.Account.Account;
import Matcher.components.Account.AccountService;
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
    //    private final HashMap<String, Account> accountList;
    private final ArrayList<Trade> tradeHistory;
    private OrderBook orderBook;
    private final AggregatedOrderBook aggregatedOrderBook;

    @Autowired
    AccountService accountService;

    @Autowired
    public Matcher() {
        tradeHistory = new ArrayList<Trade>();
        orderBook = new OrderBook();
        aggregatedOrderBook = new AggregatedOrderBook(orderBook);
    }

    public Account getAccount(String username) {
        return accountService.getAccountByUsername(username).get(0);
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
        //Account newAccount = new Account(username, startingGBP, startingBTC);
        Account account = new Account(username, password, (int) startingGBP, (int) startingBTC);
        accountService.saveOrUpdate(account);
//        if (getAccount(username) == null) {
//            accountList.put(username, newAccount);
        //passwords.addAccount(username, password);
//        }
//        System.out.println(accountList.size());
        System.out.println(accountService.getAllAccount().size());
        System.out.println(accountService.getAccountById(account.getId()).toString());
    }

    public void createAccount(String username, long startingGBP, long startingBTC) {
        createAccount(username, "password", startingGBP, startingBTC);
    }

//    private void updatePrivateOrderBooks() {
//        System.out.println(orderBook.toString());
//        for (Account account : accountList.values()
//        ) {
//            account.updateOrderBook(orderBook);
//        }
//    }

    public OrderBook getPrivateOrderBook(String username) {
        OrderBook privateOrderBook = new OrderBook();
        for (Order order : orderBook.getBuy()
        ) {
            if (order.getUsername().equals(username)) {
                privateOrderBook.addOrder(order);
            }
        }
        for (Order order : orderBook.getSell()
        ) {
            if (order.getUsername().equals(username)) {
                privateOrderBook.addOrder(order);
            }
        }
        return privateOrderBook;
    }

    public void processOrder(Order order) {
        try {
            Account currentAccount = accountService.getAccountByUsername(order.getUsername()).get(0);

            currentAccount.chargeAccount(order);
            Order remainingOrder = makeTrades(order);
            orderBook.addOrder(remainingOrder);
            orderBook.filterEmpty();
            orderBook.sortOrders();
            aggregatedOrderBook.updateBook(orderBook);
            accountService.saveOrUpdate(currentAccount);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelOrder(String username, String id, boolean cancelAll) {
        try {
            OrderBook newOrderBook = new OrderBook();
//            String username = accountService.authenticateToken(token);
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
                accountService.saveOrUpdate(getAccount(username));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelAllOrders(String username) {
        try {
//            String username = accountService.authenticateToken(token);
            Account currentAccount = getAccount(username);
            for (Order order : orderBook.getBuy()
            ) {
                if (order.getUsername().equals(username))
                    cancelOrder(username, order.getId(), true);
            }
            for (Order order : orderBook.getSell()
            ) {
                if (order.getUsername().equals(username))
                    cancelOrder(username, order.getId(), true);
            }
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
                    accountService.saveOrUpdate(buyAccount);
                    accountService.saveOrUpdate(sellAccount);
                    if (order.getVolume() < 1e-5) {
                        break;
                    }
                }
            }
        }
        return order;
    }
}
