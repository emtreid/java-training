package Matcher;

import Matcher.DTO.AccountDTO;
import Matcher.components.Account.Account;
import Matcher.components.Account.AccountService;
import Matcher.components.Account.Passwords;
import Matcher.components.Account.UserData;
import Matcher.components.AggregatedOrderBook;
import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Response;
import Matcher.components.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
public class HelloController {

    @Autowired
    public HelloController(Matcher matcher, Passwords passwords, AccountService accountService) {
        this.matcher = matcher;
        this.passwords = passwords;
        this.accountService = accountService;
    }

    private final Matcher matcher;
    private final Passwords passwords;
    private final AccountService accountService;

    private Response getResponse(int token, String username) {
        OrderBook personalOB = matcher.getPrivateOrderBook(token, username);
        AggregatedOrderBook aggregatedOrderBook = matcher.getAggregatedOrderBook();
        ArrayList<Trade> tradeHistory = matcher.getTradeHistory();
        UserData userData = new UserData(matcher.getAccount(username));
        return new Response(personalOB, aggregatedOrderBook, tradeHistory, userData);
    }

    @GetMapping("/")
    public String index() {
        return "Now accepting orders...";
    }

    @GetMapping("/orders")
    public OrderBook getOrderBook() {
        return matcher.getOrderBook();
    }

    @GetMapping("/user/{username}/orders")
    public Response getUserInfo(@PathVariable("username") String username, @RequestHeader int token) {
        System.out.println("token");
        System.out.println(token);
        return getResponse(token, username);
    }

    @PostMapping("/user/{username}")
    public int login(@PathVariable("username") String username, @RequestBody String password) throws Exception {
        System.out.println(password);
        return accountService.getToken(username, password);
    }

    @PostMapping("/user")
    public Account postAccount(@RequestBody Account account) {
        matcher.createAccount(account.getUsername(), account.getGbp(), account.getBtc());
        return matcher.getAccount(account.getUsername());
    }

    @PostMapping("/order")
    public Response postOrder(@RequestHeader int token, @RequestBody @Valid Order order) {
        System.out.println(token);
        matcher.processOrder(token, order);
        return getResponse(token, order.getUsername());
    }

}
