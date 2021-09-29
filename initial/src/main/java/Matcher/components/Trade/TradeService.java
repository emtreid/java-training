package Matcher.components.Trade;

import Matcher.components.OrderBook.OrderRepository;
import Matcher.components.OrderBook.OrderSQL;
import Matcher.util.BuyComparator;
import Matcher.util.SellComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TradeService {
    @Autowired
    TradeRepository tradeRepository;

    public List<Trade> getAllTrades() {
        List<Trade> trades = new ArrayList<Trade>();
        tradeRepository.findAll().forEach(trades::add);
        return trades;
    }

    public Trade getTradeById(int id) {
        return tradeRepository.findById(id).get();
    }

    public void saveOrUpdate(Trade trade) {
        tradeRepository.save(trade);
    }

}
