package fun.powercheng.client;

import fun.powercheng.client.sse.SseClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WsSseClientApp {
    public static void main(String[] args) {
        SpringApplication.run(WsSseClientApp.class, args);

        // 启动 sse client
        SseClient client = new SseClient("http://localhost:8000");
        client.startListening();
    }
}
