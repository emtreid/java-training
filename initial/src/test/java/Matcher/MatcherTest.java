//package Matcher;
//
//
//import Matcher.components.OrderBook.OrderSQL;
//import org.junit.Assert;
//import org.junit.Test;
//
//public class MatcherTest {
//    OrderSQL orderSQL1 = new OrderSQL("Elliott", "Buy", 15, 25);
//    OrderSQL orderSQL2 = new OrderSQL("Elliott", "Sell", 10, 20);
//    OrderSQL orderSQL3 = new OrderSQL("Andrea", "Buy", 5, 15);
//    OrderSQL orderSQL4 = new OrderSQL("Andrea", "Sell", 25, 20);
//
//
//    @Test
//    public void matcherInitialises() {
//        Matcher matcher = new Matcher();
//    }
//
//    @Test
//    public void addOrder() {
//        Matcher matcher = new Matcher();
//        matcher.createAccount("Elliott", "password", 1000, 1500);
//        matcher.processOrder("Elliottpassword".hashCode(), orderSQL1);
//        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 1);
//        matcher.processOrder("Elliottpassword".hashCode(), orderSQL2);
//        Assert.assertEquals(matcher.getOrderBook().getSell().size(), 1);
//    }
//
//    @Test
//    public void makeTrade() {
//        Matcher matcher = new Matcher();
//        matcher.createAccount("Elliott", 1000, 1500);
//        matcher.createAccount("Andrea", 1000, 1500);
//        matcher.processOrder("Elliottpassword".hashCode(), orderSQL1);
//        matcher.processOrder("Andreapassword".hashCode(), orderSQL4);
//        Assert.assertEquals(matcher.getTradeHistory().size(), 1);
//        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 0);
//    }
//
//    @Test
//    public void cancelOrder() {
//        Matcher matcher = new Matcher();
//        matcher.createAccount("Elliott", 1000, 1500);
//        matcher.createAccount("Andrea", 1000, 1500);
//        matcher.processOrder("Elliottpassword".hashCode(), orderSQL1);
//        System.out.println(matcher.getOrderBook().getBuy().size());
//        matcher.processOrder("Elliottpassword".hashCode(), orderSQL2);
//        System.out.println(matcher.getOrderBook().getBuy().size());
//        matcher.processOrder("Andreapassword".hashCode(), orderSQL3);
//        System.out.println(matcher.getOrderBook().getBuy().size());
//        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 2);
//        matcher.cancelOrder("Andreapassword".hashCode(), orderSQL3.getId(), false);
//        Assert.assertEquals(matcher.getOrderBook().getBuy().size(), 1);
//    }
//
//    @Test
//    public void chargeAndRefund() {
//        Matcher matcher = new Matcher();
//        matcher.createAccount("Elliott", 1000, 1500);
//        matcher.processOrder("Elliottpassword".hashCode(), orderSQL1);
//        Assert.assertEquals(matcher.getAccount("Elliott").getGbp(),
//                1000 - (long) orderSQL1.getPrice() * orderSQL1.getVolume());
//        matcher.cancelOrder("Elliottpassword".hashCode(), orderSQL1.getId(), false);
//        Assert.assertEquals(matcher.getAccount("Elliott").getGbp(),
//                1000);
//    }
//
//    @Test
//    public void manyOrdersConsistentBalance() {
//        Matcher matcher = new Matcher();
//        String[] users = {"Andrea", "Bob", "Elliott"};
//        int[] tokens = {"Andreapassword".hashCode(), "Bobpassword".hashCode(), "Elliottpassword".hashCode()};
//        String[] actions = {"Buy", "Sell"};
//        int[] volumes = {2, 3, 5, 4};
//        int[] prices = {4, 3, 2, 1};
//        for (String username : users
//        ) {
//            matcher.createAccount(username, 50000, 50000);
//        }
//        Assert.assertEquals(matcher.getAccount("Andrea").getGbp()
//                + matcher.getAccount("Bob").getGbp()
//                + matcher.getAccount("Elliott").getGbp(), 150000);
//        for (int i = 0; i < 100; i++) {
//            OrderSQL newOrderSQL = new OrderSQL(users[i % 3], actions[i % 2], volumes[i % 4], prices[i % 4]);
//            matcher.processOrder(tokens[i % 3], newOrderSQL);
//        }
//        System.out.println(matcher.getTradeHistory().size());
//        matcher.cancelAllOrders("Andreapassword".hashCode());
//        matcher.cancelAllOrders("Bobpassword".hashCode());
//        matcher.cancelAllOrders("Elliottpassword".hashCode());
//        Assert.assertEquals(matcher.getAccount("Andrea").getGbp()
//                + matcher.getAccount("Bob").getGbp()
//                + matcher.getAccount("Elliott").getGbp(), 150000);
//    }
//}
