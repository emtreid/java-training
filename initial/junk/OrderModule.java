package Matcher.api;

import Matcher.Matcher;
import Matcher.components.Order;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.validation.Valid;

@Component
public class OrderModule {
    private final SocketIONamespace namespace;
    private final SocketIOServer server;
    private final Matcher matcher;

    @Autowired
    public OrderModule(SocketIOServer server, Matcher matcher) {
        this.namespace = server.addNamespace("/order");
        this.server = server;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("addOrder", Order.class, onOrderReceived());
        this.matcher = matcher;
        log.debug("OrderModule Initialised");
    }

    private DataListener<Order> onOrderReceived() {
        return (client, data, ackSender) -> {
            System.out.println("addOrder");
            log.debug("Client[{}] - Received order message '{}'", client.getSessionId().toString(), data);
            matcher.processOrder(data);
            log.info("NumberBuyOrders:" + matcher.getOrderBook().getBuy().size());
            server.getBroadcastOperations().sendEvent("orderBook", matcher.getOrderBook());
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.debug("Client[{}] - Connected to order module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
            System.out.println("Connected!");
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.debug("Client[{}] - Disconnected from order module.", client.getSessionId().toString());
        };
    }

    private static final Logger log = LoggerFactory.getLogger(OrderModule.class);
}


