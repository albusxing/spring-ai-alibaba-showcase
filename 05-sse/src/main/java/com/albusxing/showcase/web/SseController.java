package com.albusxing.showcase.web;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/7/6
 */
@RestController
public class SseController {

    private final ChatClient chatClient;

    public SseController(ChatModel chatModel) {
        this.chatClient = ChatClient
            .builder(chatModel)
            // 实现 Logger 的 Advisor
            .defaultAdvisors(
                new SimpleLoggerAdvisor()
            )
            // 设置 ChatClient 中 ChatModel 的 Options 参数
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .topP(0.7)
                    .build()
            )
            .build();
    }

    @GetMapping(value = "/stream/chat")
    public Flux<String> streamChat(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return chatClient.prompt(question).stream().content();
    }


}
