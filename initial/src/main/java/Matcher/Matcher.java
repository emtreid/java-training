package Matcher;

import Matcher.components.Account.Account;
import Matcher.components.Account.AccountService;
import Matcher.components.AggregatedOrderBook;
import Matcher.components.OrderBook.OrderSQL;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.OrderBook.OrderService;
import Matcher.components.Trade.Trade;
import Matcher.components.Trade.TradeService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class Matcher {
    //    private final HashMap<String, Account> accountList;

    @Autowired
    AccountService accountService;

    @Autowired
    OrderService orderService;

    @Autowired
    TradeService tradeService;

    public Account getAccount(String username) {
        return accountService.getAccountByUsername(username).get(0);
    }

    public List<Trade> getTradeHistory() {
        return tradeService.getAllTrades();
    }

    public AggregatedOrderBook getAggregatedOrderBook() {
        return new AggregatedOrderBook(orderService);
    }

    public void createAccount(String username, String password, long startingGBP, long startingBTC) {
        Account account = new Account(username, password, (int) startingGBP, (int) startingBTC);
        accountService.saveOrUpdate(account);
    }

    public void createAccount(String username, long startingGBP, long startingBTC) {
        createAccount(username, "password", startingGBP, startingBTC);
    }

    public OrderBook getPrivateOrderBook(String username) {
        OrderBook privateOrderBook = new OrderBook();
        List<OrderSQL> userOrders = orderService.getOrderByUsername(username);
        for (OrderSQL orderSQL : userOrders) {
            privateOrderBook.addOrder(orderSQL);
        }
        return privateOrderBook;
    }

    public void processOrder(OrderSQL orderSQL) {
        try {
            Account currentAccount = accountService.getAccountByUsername(orderSQL.getUsername()).get(0);
            if (!currentAccount.checkBalance(orderSQL)) {
                throw new Exception("Insufficient balance");
            }
            currentAccount.chargeAccount(orderSQL);
            OrderSQL remainingOrderSQL = makeTrades(orderSQL);
            orderService.saveOrUpdate(remainingOrderSQL);
            orderService.filterEmpty();
            accountService.saveOrUpdate(currentAccount);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelOrder(int id) {
        try {
            OrderSQL orderSQL = orderService.getOrderById(id);
            Account currentAccount = getAccount(orderSQL.getUsername());
            currentAccount.refundOrder(orderSQL);
            orderService.delete(id);
            accountService.saveOrUpdate(currentAccount);
        } catch (
                Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void cancelAllOrders(String username) {
        try {
            for (OrderSQL orderSQL : orderService.getOrderByUsername(username)
            ) {
                cancelOrder(orderSQL.getId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addTrade(Trade newTrade) {
        tradeService.saveOrUpdate(newTrade);
    }

    private OrderSQL makeTrades(OrderSQL orderSQL) throws Exception {
        List<OrderSQL> oppositeOrderSQLBook;
        if (orderSQL.getAction().equals("Buy")) {
            oppositeOrderSQLBook = orderService.getOrderByAction("Sell");
        } else if (orderSQL.getAction().equals("Sell")) {
            oppositeOrderSQLBook = orderService.getOrderByAction("Buy");
        } else {
            throw new Exception("Action must be Buy or Sell");
        }
        for (OrderSQL previousOrderSQL : oppositeOrderSQLBook) {
            if (!orderSQL.getUsername().equals(previousOrderSQL.getUsername())) {
                System.out.println("previous order:");
                System.out.println(previousOrderSQL.toString());
                // Create and push trade, update volumes of new and previous orderSQL, until one or both are exhausted
                OrderSQL buyOrderSQL = orderSQL.getAction().equals("Buy") ? orderSQL : previousOrderSQL;
                OrderSQL sellOrderSQL = orderSQL.getAction().equals("Sell") ? orderSQL : previousOrderSQL;
                if (buyOrderSQL.getPrice() >= sellOrderSQL.getPrice()) {
                    Trade newTrade = new Trade(buyOrderSQL, sellOrderSQL);
                    Account buyAccount = getAccount(newTrade.getBuyer());
                    Account sellAccount = getAccount(newTrade.getSeller());
                    buyAccount.creditAccount(newTrade, buyOrderSQL.getPrice());
                    sellAccount.creditAccount(newTrade, buyOrderSQL.getPrice());
                    addTrade(newTrade);
                    orderSQL.setVolume(orderSQL.getVolume() - newTrade.getVolume());
                    previousOrderSQL.setVolume(previousOrderSQL.getVolume() - newTrade.getVolume());
                    orderService.saveOrUpdate(previousOrderSQL);
                    accountService.saveOrUpdate(buyAccount);
                    accountService.saveOrUpdate(sellAccount);
                    if (orderSQL.getVolume() < 1e-5) {
                        break;
                    }
                }
            }
        }
        return orderSQL;
    }
}
