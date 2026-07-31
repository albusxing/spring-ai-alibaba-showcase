package com.albusxing.showcase.web;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/7/1
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private ChatModel chatModel;


    /**
     * 没有对接百炼智能体应用
     * @param question
     * @return
     */
    @GetMapping(value = "/eat")
    public Flux<String> eat(@RequestParam(name = "msg",defaultValue = "今天吃什么") String question) {

        String info = """
                你是一个AI厨师助手,每次随机生成三个家常菜，并且提供这些家常菜的详细做法步骤，以HTML格式返回
                字数控制在1500字以内。
                """;

        // 系统消息
        SystemMessage systemMessage = new SystemMessage(info);
        // 用户消息
        UserMessage userMessage = new UserMessage(question);

        Prompt prompt = new Prompt(userMessage, systemMessage);

        Flux<ChatResponse> chatResponseFlux = chatModel.stream(prompt);
        return chatResponseFlux.map(chatResponse -> chatResponse.getResults().get(0).getOutput().getText());
    }




}
