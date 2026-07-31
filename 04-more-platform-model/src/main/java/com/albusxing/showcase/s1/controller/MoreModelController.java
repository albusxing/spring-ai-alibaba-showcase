package com.albusxing.showcase.s1.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/7/6
 */
@RestController
@RequestMapping("/more-model")
public class MoreModelController {

    //V1 通过ChatModel实现stream实现流式输出
    @Resource(name = "deepSeekChatModel")
    private ChatModel deepSeekChatModel;
    @Resource(name = "qwenChatModel")
    private ChatModel qwenChatModel;


    @GetMapping(value = "/stream/deepseek-model")
    public Flux<String> chatflux(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return deepSeekChatModel.stream(question);
    }

    @GetMapping(value = "/stream/qwen-model")
    public Flux<String> chatflux2(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return qwenChatModel.stream(question);
    }


    //V2 通过ChatClient实现stream实现流式输出
    @Resource(name = "deepSeekChatClient")
    private ChatClient deepSeekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping(value = "/stream/deepseek-client")
    public Flux<String> chatflux3(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return deepSeekChatClient.prompt(question).stream().content();
    }

    @GetMapping(value = "/stream/qwen-client")
    public Flux<String> chatflux4(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return qwenChatClient.prompt(question).stream().content();
    }


}
