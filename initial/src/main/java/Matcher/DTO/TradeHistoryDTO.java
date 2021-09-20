package Matcher.DTO;

import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Trade;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TradeHistoryDTO {
    private @Getter
    @Setter
    ArrayList<TradeDTO> tradeHistory;

    public TradeHistoryDTO(ArrayList<Trade> tradeHistory, boolean ConvertFromMatcher) {
        this.tradeHistory = new ArrayList<TradeDTO>();
        for (Trade trade : tradeHistory) {
            this.tradeHistory.add(new TradeDTO(trade));
        }
    }
}
