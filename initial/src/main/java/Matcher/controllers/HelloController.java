package Matcher.controllers;

import Matcher.DTO.AccountDTO;
import Matcher.Matcher;
import Matcher.components.Account.Account;
import Matcher.components.Account.AccountService;
import Matcher.components.Account.Passwords;
import Matcher.components.Account.UserData;
import Matcher.components.AggregatedOrderBook;
import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Response;
import Matcher.components.Trade;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@CrossOrigin("*")
public class HelloController {

    @Autowired
    public HelloController(Matcher matcher, AccountService accountService) {
        this.matcher = matcher;
        this.accountService = accountService;
    }

    private final Matcher matcher;
    private final AccountService accountService;

    private Response getResponse(String username) {
        OrderBook personalOB = matcher.getPrivateOrderBook(username);
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

    @GetMapping("/user/orders")
    public Response getUserInfo(@RequestHeader String Authorization) {
        String username = Jwts.parser()
                .setSigningKey(UserController.secretKey)
                .parseClaimsJws(Authorization.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        System.out.println("NAME");
        System.out.println(username);
        return getResponse(username);
    }

//    @PostMapping("/user/{username}")
//    public int login(@PathVariable("username") String username, @RequestBody String password) throws Exception {
//        System.out.println(password);
//        return accountService.getToken(username, password);
//    }

    @PostMapping("/user")
    public Account postAccount(@RequestBody Account account) {
        System.out.println(account.toString());
        matcher.createAccount(account.getUsername(), account.getGbp(), account.getBtc());
        return matcher.getAccount(account.getUsername());
    }

    @PostMapping("/order")
    public Response postOrder(@RequestHeader String Authorization, @RequestBody @Valid Order order) {
        String username = Jwts.parser()
                .setSigningKey(UserController.secretKey)
                .parseClaimsJws(Authorization.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        if (username.equals(order.getUsername()))
            matcher.processOrder(order);
        return getResponse(order.getUsername());
    }

}
