package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/5/16
 */
@RestController
public class ChatModelController {


    /**
     * ChatModel支持自动注入
     */
    @Resource
    private ChatModel chatModel;


    /**
     * ChatClient 不支持自动注入，无法直接使用
     */
//    @Resource
//    private ChatClient chatClient;

    @GetMapping("/chatModel/simple/chat")
    public String simpleChat(@RequestParam(name = "message", defaultValue = "介绍下你自己") String message) {

        Prompt prompt = new Prompt(message, DashScopeChatOptions.builder()
            .model("qwen3.7-max")
            .build());

        ChatResponse chatResponse = chatModel.call(prompt);

        System.out.println("响应：" + chatResponse.getMetadata());

        return chatResponse.getResult().getOutput().getText();
    }

    @GetMapping("/chatModel/stream/chat")
    public Flux<String> streamChat(@RequestParam(name = "message", defaultValue = "介绍下你自己") String message,
                             HttpServletResponse response) {

        // 避免返回乱码
        response.setCharacterEncoding("UTF-8");

        Prompt prompt = new Prompt(message, DashScopeChatOptions.builder()
            .model("qwen3.7-max")
            .build());

        Flux<ChatResponse> chatResponseFlux = chatModel.stream(prompt);

        return chatResponseFlux.map(r -> r.getResult().getOutput().getText());
    }



}
