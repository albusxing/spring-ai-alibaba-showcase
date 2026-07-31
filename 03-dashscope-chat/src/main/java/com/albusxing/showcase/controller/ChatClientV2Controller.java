package com.albusxing.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/5/16
 */
@RestController
public class ChatClientV2Controller {

    @Resource
    private ChatClient chatClient;

    /**
     * 简单调用ChatClient
     * @param message
     * @return
     */
    @GetMapping("/chatClientV2/simple/chat")
    public String simpleChat(@RequestParam(name = "message", defaultValue = "2加3等于几") String message) {
        return chatClient.prompt().user(message).call().content();
    }


    /**
     * 流式调用ChatClient
     * @param message
     * @return
     */
    @GetMapping("/chatClientV2/stream/chat")
    public Flux<String> streamChat(@RequestParam(name = "message", defaultValue = "介绍下你自己") String message) {
        return chatClient.prompt().user(message).stream().content();
    }
}
