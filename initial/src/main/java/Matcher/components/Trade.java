package Matcher.components;

import Matcher.common.IdTimeable;

import javax.validation.constraints.Min;
import java.sql.Timestamp;

public class Trade implements IdTimeable {
    private final String buyer;
    private final String seller;
    @Min(1)
    private final long volume;
    @Min(1)
    private final long price;
    private final Timestamp timestamp;
    private final String id;

    public Trade(Order buyOrder, Order sellOrder) {
        this(buyOrder.getUsername(), sellOrder.getUsername(),
                Math.min(buyOrder.getVolume(), sellOrder.getVolume()), sellOrder.getPrice());
    }

    public Trade(String buyer, String seller, long volume, long price) {
        this.buyer = buyer;
        this.seller = seller;
        this.volume = volume;
        this.price = price;
        timestamp = new Timestamp(System.currentTimeMillis());
        id = buyer + seller + timestamp.toString() + Math.random();
    }


    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public long getVolume() {
        return volume;
    }

    public long getPrice() {
        return price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

}