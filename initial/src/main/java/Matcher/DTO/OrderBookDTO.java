package Matcher.DTO;

import Matcher.components.OrderBook.OrderSQL;
import Matcher.components.OrderBook.OrderBook;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderBookDTO {
    private @Getter
    @Setter
    List<OutOrderDTO> buy;
    private @Getter
    @Setter
    List<OutOrderDTO> sell;

    public OrderBookDTO(OrderBook orderBook) {
        buy = new ArrayList<OutOrderDTO>();
        sell = new ArrayList<OutOrderDTO>();
        for (OrderSQL orderSQL : orderBook.getBuy()
        ) {
            buy.add(new OutOrderDTO(orderSQL));
        }
        for (OrderSQL orderSQL : orderBook.getSell()
        ) {
            sell.add(new OutOrderDTO(orderSQL));
        }
    }
}
