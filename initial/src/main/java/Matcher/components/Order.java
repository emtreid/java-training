package Matcher.components;

import Matcher.common.IdTimeable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order implements IdTimeable {
    private final String username;
    private final String action;
    private double volume;
    private final double price;
    private final Timestamp timestamp;
    private final String id;

    public Order(String username, String action, double volume, double price) {
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

    public double getVolume() {
        return volume;
    }

    public double getPrice() {
        return price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public void setVolume(double newVolume) {
        volume = newVolume;
    }
}
