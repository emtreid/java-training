package Matcher.components;

import Matcher.components.Account.UserData;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Trade.Trade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Response {
    private @Getter
    @Setter
    OrderBook personalOrderBook;
    private @Getter
    @Setter
    AggregatedOrderBook aggregatedOrderBook;
    private @Getter
    @Setter
    List<Trade> tradeHistory;
    private @Getter
    @Setter
    UserData userData;
}
