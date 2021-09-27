package Matcher.components.OrderBook;

import Matcher.util.BuyComparator;
import Matcher.util.SellComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public List<OrderSQL> getAllOrders() {
        List<OrderSQL> ordersSQL = new ArrayList<OrderSQL>();
        orderRepository.findAll().forEach(ordersSQL::add);
        return ordersSQL;
    }

    public OrderSQL getOrderById(int id) {
        return orderRepository.findById(id).get();
    }

    public List<OrderSQL> getOrderByUsername(String username) {
        return orderRepository.findByUsername(username);
    }

    public void filterEmpty() {
        List<OrderSQL> emptyOrders = orderRepository.findByVolume(0);
        for (OrderSQL orderSQL : emptyOrders
        ) {
            delete(orderSQL.getId());
        }
    }

    public List<OrderSQL> getOrderByAction(String action) {
        List<OrderSQL> orderSQLList = orderRepository.findByOrderAction(action);
        if (action.equals("Buy")) {
            orderSQLList.sort(new BuyComparator());
        } else if (action.equals("Sell")) {
            orderSQLList.sort(new SellComparator());
        }
        return orderSQLList;
    }

    public void saveOrUpdate(OrderSQL orderSQL) {
        if (orderRepository.findById(orderSQL.getId()).isPresent()) {
            delete(orderSQL.getId());
        }
        orderRepository.save(orderSQL);
//        System.out.println("currentLength" + getAllOrders().size());
    }

    public void delete(int id) {
        orderRepository.deleteById(id);
    }

}
