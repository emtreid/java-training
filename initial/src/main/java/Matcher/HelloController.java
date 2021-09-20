package Matcher;

import Matcher.components.Account.Account;
import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class HelloController {

    @Autowired
    public HelloController(Matcher matcher) {
        this.matcher = matcher;
    }

    private final Matcher matcher;

    @GetMapping("/")
    public String index() {
        return "Now accepting orders...";
    }

    @GetMapping("/orders")
    public OrderBook getOrderBook() {
        return matcher.getOrderBook();
    }

    @PostMapping("/user")
    public Account postAccount(@RequestBody @Valid Account account) {
        matcher.createAccount(account.getUsername(), account.getBalanceGBP(), account.getBalanceBTC());
        return matcher.getAccount(account.getUsername());
    }

    @PostMapping("/order")
    public Order postOrder(@RequestHeader int token, @RequestBody @Valid Order order) {
        matcher.processOrder(token, order);
        return order;
    }

}
