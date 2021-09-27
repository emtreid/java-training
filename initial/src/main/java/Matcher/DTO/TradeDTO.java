package Matcher.DTO;

import Matcher.components.Trade.Trade;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TradeDTO {

    private @Getter
    @Setter
    String buyer;
    private @Getter
    @Setter
    String seller;
    private @Getter
    @Setter
    long volume;
    private @Getter
    @Setter
    long price;
    private @Getter
    @Setter
    Timestamp timestamp;
    private @Getter
    @Setter
    int id;

    public TradeDTO(Trade trade) {
        this(trade.getBuyer(), trade.getSeller(), trade.getVolume(), trade.getPrice(), trade.getTimestamp(), trade.getId());
    }
}
