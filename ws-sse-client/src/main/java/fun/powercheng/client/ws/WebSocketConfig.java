package fun.powercheng.client.ws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebSocketConfig {

    @Value("${ws.url}")
    private String url;

    @Bean
    public StandardWebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public ReconnectableWebSocketHandler echoWebSocketHandler(StandardWebSocketClient client) {
        return new ReconnectableWebSocketHandler(client, url);
    }

    @Bean
    public WebSocketConnectionManager connectionManager(StandardWebSocketClient client, ReconnectableWebSocketHandler handler) {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, handler, url);
        manager.setAutoStartup(true);
        return manager;
    }
}