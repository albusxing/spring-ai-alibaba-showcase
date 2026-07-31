package com.albusxing.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author Albusxing
 * @created 2026/6/22
 * https://java2ai.com/docs/1.0.0.2/tutorials/basics/prompt/?spm=5176.29160081.0.0.2856aa5cdeol7a
 *
 */
@RestController
public class PromptController {

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;


    /**
     * prompt/chat?question=火锅
     *
     * @param question
     * @return
     */
    @GetMapping("/prompt/chat")
    public Flux<String> chat(@RequestParam("question") String question) {
        return deepseekChatClient.prompt()
            // AI 能力边界
            .system("你是一个法律助手，只回答法律问题，其它问题回复，我只能回答法律相关问题，其它无可奉告")
            .user(question)
            .stream()
            .content();
    }

    /**
     * /prompt/chat2?question=葫芦娃
     *
     * @param question
     * @return
     */
    @GetMapping("/prompt/chat2")
    public Flux<ChatResponse> chat2(@RequestParam("question") String question) {
        // 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手,每个故事控制在300字以内");

        // 用户消息
        UserMessage userMessage = new UserMessage(question);

        Prompt prompt = new Prompt(userMessage, systemMessage);

        return deepseekChatModel.stream(prompt);

    }

    /**
     * /prompt/chat3?question=葫芦娃
     *
     * @param question
     * @return
     */
    @GetMapping("/prompt/chat3")
    public Flux<String> chat3(@RequestParam("question") String question) {
        // 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手," +
            "每个故事控制在600字以内且以HTML格式返回");

        // 用户消息
        UserMessage userMessage = new UserMessage(question);

        Prompt prompt = new Prompt(userMessage, systemMessage);

        return deepseekChatModel.stream(prompt)
            .map(response -> response.getResults().get(0).getOutput().getText());

    }

    /**
     * /prompt/chat4?question=葫芦娃
     *
     * @param question
     * @return
     */
    @GetMapping("/prompt/chat4")
    public String chat4(@RequestParam("question") String question) {
        return deepseekChatClient.prompt()
            .system("你是一个讲故事的助手,每个故事控制在600字以内且以HTML格式返回")
            .user(question)
            .call()
            .content();
    }

}
