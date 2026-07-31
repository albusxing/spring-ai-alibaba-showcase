package com.albusxing.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/5/15
 */
@RestController
@RequestMapping("/hello")
public class HelloworldController {


    @Resource
    private ChatModel chatModel;


    /**
     * 聊天
     * @param message
     * @return
     */
    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        return chatModel.call(message);
    }


    /**
     * 流式聊天
     * @param message
     * @return
     */
    @GetMapping("/stream/chat")
    public Flux<String> streamChat(@RequestParam(name = "message", defaultValue = "你是谁") String message) {
        return chatModel.stream(message);
    }
}
