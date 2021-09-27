package Matcher.components.Trade;

import Matcher.components.OrderBook.OrderSQL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TradeRepository extends CrudRepository<Trade, Integer> {
}