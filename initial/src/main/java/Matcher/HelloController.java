package Matcher;

import Matcher.components.Account;
import Matcher.components.Order;
import Matcher.components.OrderBook.OrderBook;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

	Matcher matcher = new Matcher();

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/orders")
	public OrderBook getOrderBook () {
		OrderBook orderBook = matcher.getOrderBook();
		return orderBook;
	}

	@PostMapping("/user")
	public Account postAccount(@RequestParam String username, int startingGBP, int startingBTC){
		matcher.createAccount(username, startingGBP, startingBTC);
		return matcher.getAccount(username);
	}

	@PostMapping("/order")
	public Order postOrder (@RequestParam String username, String action, int volume, int price) {
		Order order = new Order(username, action, volume, price);
		matcher.processOrder(order);
		return order;
	}

}

class NewOrder {
	String username;
	String action;
	double volume;
	double price;
}
