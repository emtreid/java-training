package Matcher.controllers;

import Matcher.DTO.OutOrderDTO;
import Matcher.Matcher;
import Matcher.components.Account.Account;
import Matcher.components.Account.AccountService;
import Matcher.components.Account.UserData;
import Matcher.components.AggregatedOrderBook;
import Matcher.components.OrderBook.OrderService;
import Matcher.components.OrderBook.OrderSQL;
import Matcher.components.OrderBook.OrderBook;
import Matcher.components.Response;
import Matcher.components.Trade.Trade;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class HelloController {

    @Autowired
    public HelloController(Matcher matcher, AccountService accountService, OrderService orderService) {
        this.matcher = matcher;
        this.accountService = accountService;
        this.orderService = orderService;
    }

    private final Matcher matcher;
    private final AccountService accountService;
    private final OrderService orderService;

    private Response getResponse(String username) {
        OrderBook personalOB = matcher.getPrivateOrderBook(username);
        AggregatedOrderBook aggregatedOrderBook = matcher.getAggregatedOrderBook();
        List<Trade> tradeHistory = matcher.getTradeHistory();
        UserData userData = new UserData(matcher.getAccount(username));
        System.out.println("GotResponse");
        return new Response(personalOB, aggregatedOrderBook, tradeHistory, userData);
    }

    @GetMapping("/")
    public String index() {
        return "Now accepting orderSQL...";
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
    public Response postOrder(@RequestHeader String Authorization, @RequestBody @Valid OutOrderDTO OutOrderDTO) {
        String username = Jwts.parser()
                .setSigningKey(UserController.secretKey)
                .parseClaimsJws(Authorization.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        OrderSQL orderSQL = new OrderSQL(OutOrderDTO);
        System.out.println(orderSQL.toString());
        if (username.equals(orderSQL.getUsername()) || username.equals("admin"))
            matcher.processOrder(orderSQL);
        System.out.println("processed");
        return getResponse(username);
    }

    @PutMapping("/user/{username}/deposit/{currency}")
    private Response topUp(@RequestHeader String Authorization, @PathVariable("username") String username,
                           @PathVariable("currency") String currency, @RequestBody long amount) throws Exception {
        String authorisedUsername = Jwts.parser()
                .setSigningKey(UserController.secretKey)
                .parseClaimsJws(Authorization.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        if (!authorisedUsername.equals(username)) throw new Exception("Unauthorised");
        Account account = accountService.getAccountByUsername(username).get(0);
        if (currency.equals("GBP")) {
            account.addGBP(amount * 10000);
        } else if (currency.equals("BTC")) {
            account.addBTC(amount * 100);
        }
        accountService.saveOrUpdate(account);
        return getResponse(username);
    }

    @DeleteMapping("/order/{id}")
    private Response deleteOrder(@RequestHeader String Authorization, @PathVariable("id") int id) {
        String username = Jwts.parser()
                .setSigningKey(UserController.secretKey)
                .parseClaimsJws(Authorization.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        String orderUsername = orderService.getOrderById(id).getUsername();
        if (orderUsername.equals(username))
            matcher.cancelOrder(id);
        return getResponse(username);
    }
}
