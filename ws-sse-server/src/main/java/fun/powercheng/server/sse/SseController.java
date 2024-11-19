package fun.powercheng.server.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/sse")
public class SseController {

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        // 创建一个 SseEmitter 对象，默认超时时间为30秒
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // 后台持续推送消息
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    emitter.send(SseEmitter.event().data("{\"msg\": \"hello,sse!\"}", MediaType.APPLICATION_JSON));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // 当连接关闭时执行的操作
        emitter.onCompletion(() -> System.out.println("Connection closed."));

        // 返回 SseEmitter 给客户端保持长连接
        return emitter;
    }
}
