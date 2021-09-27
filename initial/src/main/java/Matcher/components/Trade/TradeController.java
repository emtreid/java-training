package Matcher.components.Trade;

import Matcher.components.OrderBook.OrderSQL;
import Matcher.components.OrderBook.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class TradeController {
    @Autowired
    TradeService tradeService;

    @GetMapping("/trades")
    private List<Trade> getAllTrades() {
        return tradeService.getAllTrades();
    }

    @GetMapping("/trade/{id}")
    private Trade trade(@PathVariable("id") int id) {
        return tradeService.getTradeById(id);
    }

    @PostMapping("/trade")
    private int saveOrder(@RequestBody Trade trade) {
        tradeService.saveOrUpdate(trade);
        return trade.getId();
    }
}
