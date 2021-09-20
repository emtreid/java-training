package Matcher.components;

import Matcher.DTO.InOrderDTO;
import Matcher.DTO.OutOrderDTO;
import Matcher.common.IdTimeable;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

public class Order implements IdTimeable {
    @NotNull
    private final String username;
    @Pattern(regexp = "Buy|Sell")
    private final String action;
    @Min(0)
    private long volume; //volume*10
    @Min(1)
    private final long price; //price*100
    private final Timestamp timestamp;
    private final String id;

    public Order(String username, String action, @Valid long volume, @Valid long price) {
        this.username = username;
        this.action = action;
        this.volume = volume;
        this.price = price;
        timestamp = new Timestamp(System.currentTimeMillis());
        id = username + timestamp + Math.random();
    }

    public Order(OutOrderDTO inOrderDTO) {
        this(inOrderDTO.getUsername(), inOrderDTO.getAction(), inOrderDTO.getVolume(), inOrderDTO.getPrice());
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
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

    public void setVolume(long newVolume) {
        volume = newVolume;
    }
}
