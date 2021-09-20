package Matcher.DTO;

import Matcher.components.Order;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OutOrderDTO {

    private @Getter
    @Setter
    String username;
    private @Getter
    @Setter
    String action;
    private @Getter
    @Setter
    long volume;
    private @Getter
    @Setter
    long price;
    private @Getter
    @Setter
    String timestamp;
    private @Getter
    @Setter
    String id;

    public OutOrderDTO(Order order) {
        this(order.getUsername(), order.getAction(), order.getVolume(), order.getPrice(), order.getTimestamp().toString(), order.getId());
    }
}
