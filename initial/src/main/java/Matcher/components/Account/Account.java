package Matcher.components.Account;

import Matcher.components.OrderBook.OrderSQL;
import Matcher.components.Trade.Trade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@ToString
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    @Column
    @Getter
    private int id;

    @Column
    @Getter
    @Setter
    private String username;

    @Column
    private @Getter
    @Setter
    String password;

    @Column
    @Getter
    @Setter
    private int gbp;

    @Column
    @Getter
    @Setter
    private int btc;

    public Account(String username, String password, int gbp, int btc) {
        this.username = username;
        this.password = password;
        this.gbp = gbp;
        this.btc = btc;
    }

    public void addGBP(long amount) {
        gbp += amount;
    }

    public void addBTC(long amount) {
        btc += amount;
    }

    public void subtractGBP(long amount) {
        gbp -= amount;
    }

    public void subtractBTC(long amount) {
        btc -= amount;
    }

    public boolean checkBalance(String currency, long amount) {
        if (currency.equals("gbp")) {
            return (gbp >= amount);
        } else if (currency.equals("btc")) {
            return (btc >= amount);
        } else return false;
    }

    public boolean checkBalance(OrderSQL orderSQL) {
        if (orderSQL.getAction().equals("Buy")) {
            return checkBalance("gbp", orderSQL.getPrice() * orderSQL.getVolume());
        } else if (orderSQL.getAction().equals("Sell")) {
            return checkBalance("btc", orderSQL.getVolume());
        } else return false;
    }

    public void withdraw(String currency, long amount) throws Exception {
        if (!checkBalance(currency, amount)) {
            throw new Exception("Insufficient funds");
        } else {
            if (currency.equals("gbp")) {
                subtractGBP(amount);
            } else if (currency.equals("btc")) {
                subtractBTC(amount);
            }
        }
    }

    public void chargeAccount(OrderSQL orderSQL) {
        if (orderSQL.getAction().equals("Buy")) {
            long cost = orderSQL.getPrice() * orderSQL.getVolume();
            subtractGBP(cost);
        } else if (orderSQL.getAction().equals("Sell")) {
            subtractBTC(orderSQL.getVolume());
        }
    }

    public void creditAccount(Trade trade, long buyOrderPrice) {
        System.out.println("crediting");
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

    public void refundOrder(OrderSQL orderSQL) {
        if (orderSQL.getAction().equals("Buy")) {
            long cost = orderSQL.getPrice() * orderSQL.getVolume();
            addGBP(cost);
        } else if (orderSQL.getAction().equals("Sell")) {
            addBTC(orderSQL.getVolume());
        }
    }
}
