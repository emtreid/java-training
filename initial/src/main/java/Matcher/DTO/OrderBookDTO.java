package Matcher.DTO;

import Matcher.components.Order;
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
        for (Order order : orderBook.getBuy()
        ) {
            buy.add(new OutOrderDTO(order));
        }
        for (Order order : orderBook.getSell()
        ) {
            sell.add(new OutOrderDTO(order));
        }
    }
}
