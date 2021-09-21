package Matcher.components.Account;

import Matcher.components.Order;
import Matcher.components.Trade;
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
    private int token;

    @Column
    @Getter
    @Setter
    private int gbp;

    @Column
    @Getter
    @Setter
    private int btc;

    public Account(String username, String password, int token, int gbp, int btc) {
        this.username = username;
        this.password = password;
        this.token = token;
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

    public void chargeAccount(Order order) {
        if (order.getAction().equals("Buy")) {
            long cost = order.getPrice() * order.getVolume();
            subtractGBP(cost);
        } else if (order.getAction().equals("Sell")) {
            subtractBTC(order.getVolume());
        }
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
}
