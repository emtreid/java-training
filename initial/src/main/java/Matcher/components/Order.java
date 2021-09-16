package Matcher.components;

import Matcher.common.IdTimeable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order implements IdTimeable {
    private final String username;
    private final String action;
    @Min(0)
    private int volume; //volume*10
    @Min(1)
    private final int price; //price*10
    private final Timestamp timestamp;
    private final String id;

    public Order(String username, String action, int volume, int price) {
        this.username = username;
        this.action = action;
        this.volume = volume;
        this.price = price;
        timestamp = new Timestamp(System.currentTimeMillis());
        id = username + timestamp.toString() + Math.random();
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public int getVolume() {
        return volume;
    }

    public int getPrice() {
        return price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public void setVolume(int newVolume) {
        volume = newVolume;
    }
}
