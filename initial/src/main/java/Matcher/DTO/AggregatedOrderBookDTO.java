package Matcher.DTO;

import Matcher.components.AggregatedOrderBook;
import lombok.*;

import java.util.TreeMap;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AggregatedOrderBookDTO {
    private @Getter
    @Setter
    TreeMap<Long, Long> Buy;
    private @Getter
    @Setter
    TreeMap<Long, Long> Sell;

    public AggregatedOrderBookDTO(AggregatedOrderBook aggregatedOrderBook) {
        Buy = aggregatedOrderBook.getBuy();
        Sell = aggregatedOrderBook.getSell();
    }
}
