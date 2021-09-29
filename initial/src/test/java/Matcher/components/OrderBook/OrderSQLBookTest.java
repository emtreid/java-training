//package Matcher.components.OrderBook;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//public class OrderSQLBookTest {
//    OrderSQL orderSQL1 = new OrderSQL("user1", "Buy", 5, 1);
//    OrderSQL orderSQL2 = new OrderSQL("user1", "Buy", 0, 2);
//    OrderSQL orderSQL3 = new OrderSQL("user1", "Sell", 10, 5);
//    OrderSQL orderSQL4 = new OrderSQL("user2", "Buy", 2, 3);
//    OrderSQL orderSQL5 = new OrderSQL("user2", "Sell", 2, 4);
//    OrderSQL orderSQL6 = new OrderSQL("user3", "Sell", 2, 6);
//
//    @Test
//    public void createOrderBook() {
//        OrderBook orderBook = new OrderBook();
//    }
//
//    @Test
//    public void populateOrderBook() {
//        OrderBook orderBook = new OrderBook();
//        orderBook.addOrder(orderSQL1);
//        orderBook.addOrder(orderSQL2);
//        orderBook.addOrder(orderSQL3);
//        orderBook.addOrder(orderSQL4);
//        ArrayList<OrderSQL> buyList = orderBook.getBuy();
//        ArrayList<OrderSQL> sellList = orderBook.getSell();
//        Assert.assertEquals(buyList.size(), 3);
//        Assert.assertEquals(sellList.size(), 1);
//    }
//
//    @Test
//    public void removeEmptyOrders() {
//        OrderBook orderBook = new OrderBook();
//        orderBook.addOrder(orderSQL1);
//        orderBook.addOrder(orderSQL2);
//        orderBook.addOrder(orderSQL3);
//        orderBook.addOrder(orderSQL4);
//        orderBook.filterEmpty();
//        ArrayList<OrderSQL> buyList = orderBook.getBuy();
//        Assert.assertEquals(buyList.size(), 2);
//    }
//
//    @Test
//    public void sortOrders() {
//        OrderBook orderBook = new OrderBook();
//        orderBook.addOrder(orderSQL1);
//        orderBook.addOrder(orderSQL2);
//        orderBook.addOrder(orderSQL3);
//        orderBook.addOrder(orderSQL4);
//        orderBook.addOrder(orderSQL5);
//        orderBook.addOrder(orderSQL6);
//        orderBook.sortOrders();
//        Assert.assertEquals(orderBook.getBuy().get(0).getPrice(), 3);
//        Assert.assertEquals(orderBook.getSell().get(0).getPrice(), 4);
//    }
//}
