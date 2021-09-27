package Matcher.components.OrderBook;

import Matcher.util.BuyComparator;
import Matcher.util.SellComparator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class OrderBook {
    private ArrayList<OrderSQL> Buy;
    private ArrayList<OrderSQL> Sell;

    public OrderBook() {
        Buy = new ArrayList<OrderSQL>();
        Sell = new ArrayList<OrderSQL>();
    }

    @JsonProperty("Buy")
    public ArrayList<OrderSQL> getBuy() {
        return Buy;
    }

    @JsonProperty("Sell")
    public ArrayList<OrderSQL> getSell() {
        return Sell;
    }

    public void emptyBook() {
        Buy = new ArrayList<OrderSQL>();
        Sell = new ArrayList<OrderSQL>();
    }

    public void addOrder(OrderSQL newOrderSQL) {
        if (newOrderSQL.getAction().equals("Buy")) {
            Buy.add(newOrderSQL);
        } else if (newOrderSQL.getAction().equals("Sell")) {
            Sell.add(newOrderSQL);
        }
    }

    public void sortOrders() {
        Buy.sort(new BuyComparator());
        Sell.sort(new SellComparator());
    }

    public void filterEmpty() {
        ArrayList<OrderSQL> newBuy = new ArrayList<OrderSQL>();
        ArrayList<OrderSQL> newSell = new ArrayList<OrderSQL>();
        for (OrderSQL orderSQL : Buy
        ) {
            if (orderSQL.getVolume() > 1e-5) {
                newBuy.add(orderSQL);
            }
        }
        for (OrderSQL orderSQL : Sell
        ) {
            if (orderSQL.getVolume() > 1e-5) {
                newSell.add(orderSQL);
            }
        }
        Buy = newBuy;
        Sell = newSell;
    }
}
