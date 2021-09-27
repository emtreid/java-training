package Matcher.DTO;

import Matcher.components.Trade.Trade;
import lombok.*;

import java.util.ArrayList;

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
