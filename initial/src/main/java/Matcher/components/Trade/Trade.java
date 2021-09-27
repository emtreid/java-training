package Matcher.components.Trade;

import Matcher.components.OrderBook.OrderSQL;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Entity
@Table
@ToString
@Setter
@NoArgsConstructor
public class Trade {
    @Id
    @GeneratedValue
    @Getter
    @Column
    private int id;
    @Column
    @Getter
    private String buyer;
    @Column
    @Getter
    private String seller;
    @Column
    @Getter
    @Min(1)
    private long volume;
    @Column
    @Getter
    @Min(1)
    private long price;
    @Column
    @Getter
    private Timestamp tradetime;

    public Trade(OrderSQL buyOrderSQL, OrderSQL sellOrderSQL) {
        this(buyOrderSQL.getUsername(), sellOrderSQL.getUsername(),
                Math.min(buyOrderSQL.getVolume(), sellOrderSQL.getVolume()), sellOrderSQL.getPrice());
    }

    public Trade(String buyer, String seller, long volume, long price) {
        this.buyer = buyer;
        this.seller = seller;
        this.volume = volume;
        this.price = price;
        tradetime = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getTimestamp() {
        return tradetime;
    }
}