package Matcher.DTO;

import Matcher.components.OrderBook.OrderSQL;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OutOrderDTO {
    
    private String username;

    private String action;

    private long volume;

    private long price;

    private String timestamp;

    private int id;

    public OutOrderDTO(OrderSQL orderSQL) {
        this(orderSQL.getUsername(), orderSQL.getAction(), orderSQL.getVolume(), orderSQL.getPrice(), orderSQL.getTimestamp().toString(), orderSQL.getId());
    }
}
