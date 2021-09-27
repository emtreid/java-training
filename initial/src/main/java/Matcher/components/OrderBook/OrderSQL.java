package Matcher.components.OrderBook;

import Matcher.DTO.OutOrderDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@ToString
@NoArgsConstructor
public class OrderSQL {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @Getter
    private int id;

    @Column
    @Getter
    @Setter
    private String username;

    @Column
    private String orderAction;

    @Column
    @Getter
    @Setter
    private long price;

    @Column
    @Getter
    @Setter
    private long volume;

    @Column
    private Timestamp orderTime;

    public OrderSQL(String username, String orderAction, long price, long volume) {
        this.username = username;
        this.orderAction = orderAction;
        this.price = price;
        this.volume = volume;
        this.orderTime = new Timestamp(System.currentTimeMillis());
        //this.id = username + orderTime + Math.random();
    }

    public OrderSQL(OutOrderDTO OutOrderDTO) {
        this(OutOrderDTO.getUsername(), OutOrderDTO.getAction(), OutOrderDTO.getPrice(), OutOrderDTO.getVolume());
    }

    public String getAction() {
        return orderAction;
    }

    public void setAction(String orderAction) throws Exception {
        if (orderAction.equals("Buy") || orderAction.equals("Sell"))
            this.orderAction = orderAction;
        else throw new Exception("Order error: action must be Buy or Sell");
    }

    public Timestamp getTimestamp() {
        return orderTime;
    }

    public void setTimestamp(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
}
