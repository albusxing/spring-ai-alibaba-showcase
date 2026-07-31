package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/5/16
 */
@RestController
public class ChatClientController {

    /**
     * ChatClient不支持自动注入，依赖ChatModel对象接口
     */
    private final ChatClient chatClient;

    // 方式1：使用ChatModel对象创建ChatClient
    public ChatClientController(ChatModel chatModel) {
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

    // 方式2：使用ChatClient.Builder创建ChatClient
//    public ChatClientController(ChatClient.Builder builder) {
//        this.chatClient = builder
//            .build();
//    }

    /**
     * 简单调用ChatClient
     * @param message
     * @return
     */
    @GetMapping("/chatClient/simple/chat")
    public String simpleChat(@RequestParam(name = "message", defaultValue = "介绍下你自己") String message) {
        return chatClient.prompt().user(message).call().content();
    }


    /**
     * 流式调用ChatClient
     * @param message
     * @return
     */
    @GetMapping("/chatClient/stream/chat")
    public Flux<String> streamChat(@RequestParam(name = "message", defaultValue = "2加3等于几") String message) {
        return chatClient.prompt().user(message).stream().content();
    }
}
