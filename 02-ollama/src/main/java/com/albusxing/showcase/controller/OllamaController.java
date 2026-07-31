package com.albusxing.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/5/16
 */
@RestController
public class OllamaController {


    @Qualifier("ollamaChatModel")
    @Resource
    private ChatModel chatModel;


    @GetMapping("/ollama/chat")
    public String chat(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        String result = chatModel.call(message);
        System.out.println("---结果：" + result);
        return result;
    }

    @GetMapping("/ollama/streamChat")
    public Flux<String> streamChat(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        return chatModel.stream(message);
    }
}
