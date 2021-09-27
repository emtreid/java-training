package Matcher.components.OrderBook;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderSQL, Integer> {
    List<OrderSQL> findByUsername(String username);

    List<OrderSQL> findByVolume(long volume);

    List<OrderSQL> findByOrderAction(String orderAction);
}