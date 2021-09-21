package Matcher.components;

import Matcher.components.Account.UserData;
import Matcher.components.OrderBook.OrderBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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
    ArrayList<Trade> tradeHistory;
    private @Getter
    @Setter
    UserData userData;
}
