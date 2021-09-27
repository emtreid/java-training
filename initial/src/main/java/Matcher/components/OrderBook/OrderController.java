package Matcher.components.OrderBook;

import Matcher.controllers.UserController;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/ordersql")
    private List<OrderSQL> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/ordersql/{id}")
    private OrderSQL getOrder(@PathVariable("id") int id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/ordersql")
    private int saveOrder(@RequestBody OrderSQL orderSQL) {
        orderService.saveOrUpdate(orderSQL);
        return orderSQL.getId();
    }
}
