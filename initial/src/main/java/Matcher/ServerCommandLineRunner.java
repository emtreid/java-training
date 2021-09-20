package Matcher;

import Matcher.DTO.*;
import Matcher.components.Account.Passwords;
import Matcher.components.Order;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class ServerCommandLineRunner implements CommandLineRunner {

    private final Matcher matcher;
    private final Passwords passwords;
    private final SocketIOServer server;

    @Autowired
    public ServerCommandLineRunner(SocketIOServer server, Matcher matcher, Passwords passwords) {
        this.server = server;
        this.matcher = matcher;
        this.passwords = passwords;
    }

    @Override
    public void run(String... args) throws Exception {

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                HandshakeData handshakeData = client.getHandshakeData();
                System.out.println("Client[{}] - Connected to order module through '{}'" + client.getSessionId().toString() + handshakeData.getUrl());
                System.out.println("Connected");
            }
        });
//        server.addEventListener("message", String.class, new DataListener<String>() {
//            @Override
//            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
//                System.out.println(data);
//                client.sendEvent("message", "thanks");
//            }
//        });

        server.addEventListener("switchUser", LoginDTO.class, new DataListener<LoginDTO>() {
            @Override
            public void onData(SocketIOClient client, LoginDTO data, AckRequest ackSender) {
                try {
                    int token = passwords.getToken(data.getUsername(), data.getPassword());
                    System.out.println(token);
                    client.sendEvent("userToken", token);
                    client.sendEvent("personalOB", new OrderBookDTO(matcher.getAccount(data.getUsername()).getPrivateOrderBook()));
                    client.sendEvent("aggregatedOB", new AggregatedOrderBookDTO(matcher.getAggregatedOrderBook()));
                    client.sendEvent("tradeHistory", new TradeHistoryDTO(matcher.getTradeHistory(), true));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    client.sendEvent("error", e.getMessage());
                }
            }
        });

        server.addEventListener("cancelOrder", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                //matcher.cancelOrder(data, false);
                server.getBroadcastOperations().sendEvent("aggregatedOB", new AggregatedOrderBookDTO(matcher.getAggregatedOrderBook()));
                server.getBroadcastOperations().sendEvent("tradeHistory", new TradeHistoryDTO(matcher.getTradeHistory(), true));
            }
        });
        //server.addDisconnectListener(onDisconnected());
        server.addEventListener("addOrder", OutOrderDTO.class, new DataListener<OutOrderDTO>() {
            @Override
            public void onData(SocketIOClient client, OutOrderDTO data, AckRequest ackSender) throws Exception {
                System.out.println(client.getHandshakeData().getUrl());
                Order order = new Order(data);
                matcher.processOrder(1, order);
                server.getBroadcastOperations().sendEvent("aggregatedOB", new AggregatedOrderBookDTO(matcher.getAggregatedOrderBook()));
                server.getBroadcastOperations().sendEvent("tradeHistory", new TradeHistoryDTO(matcher.getTradeHistory(), true));
                server.getBroadcastOperations().sendEvent("personalOB", new OrderBookDTO(matcher.getAccount(data.getUsername()).getPrivateOrderBook()));
            }
        });

        server.start();
    }
}