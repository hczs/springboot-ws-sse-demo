package fun.powercheng.server;

import fun.powercheng.server.ws.MyWebSocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class WsServerApp {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WsServerApp.class, args);
        MyWebSocketHandler.sendMessageToAllClients("hello, Websocket");
    }
}
