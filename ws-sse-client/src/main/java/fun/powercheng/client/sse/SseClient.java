package fun.powercheng.client.sse;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

public class SseClient {

    private final WebClient webClient;

    public SseClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public void startListening() {
        webClient.get()
                .uri("/sse/connect")
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToFlux(String.class) // 服务器消息转为String
                .doOnNext(System.out::println) // 处理接收到的消息
                .doOnError(System.err::println) // 处理错误
                .retryWhen(Retry.backoff(Integer.MAX_VALUE, Duration.ofSeconds(5))) // 错误重试策略
                .blockLast(); // 阻塞等待直到流结束或错误发生
    }

}
