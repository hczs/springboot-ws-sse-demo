package fun.powercheng.client.ws;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;

import java.util.concurrent.ExecutionException;

public class ReconnectableWebSocketHandler implements WebSocketHandler {

    private final WebSocketClient webSocketClient;
    private final String url;

    public ReconnectableWebSocketHandler(WebSocketClient webSocketClient, String url) {
        this.webSocketClient = webSocketClient;
        this.url = url;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立后执行的操作，例如发送一条消息
        session.sendMessage(new TextMessage("Hello Server!"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 接收到消息时执行的操作
        System.out.println("Received message from server: " + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        // 错误处理逻辑
        System.out.println("连接错误，正在重连...");
        reconnect();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 连接关闭后的操作
        System.out.println("Connection closed, attempting to reconnect...");
        reconnect();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void reconnect() {
        try {
            Thread.sleep(2000);
            System.out.println("Reconnecting...");
            ListenableFuture<WebSocketSession> connFuture = webSocketClient.doHandshake(this, url);
            connFuture.addCallback(new ListenableFutureCallback<WebSocketSession>() {
                @Override
                public void onFailure(Throwable throwable) {
                    System.out.println("Connection error occurred, attempting to reconnect...");
                    try {
                        Thread.sleep(2000);
                        System.out.println("Reconnecting...");
                        reconnect();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onSuccess(WebSocketSession webSocketSession) {
                    System.out.println("Reconnected successfully");
                }
            });
            connFuture.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Reconnection interrupted: " + e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("Reconnection failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}