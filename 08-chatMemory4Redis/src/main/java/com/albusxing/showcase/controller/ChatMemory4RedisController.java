package com.albusxing.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Albusxing
 * @created 2026/6/22
 */
@RestController
public class ChatMemory4RedisController {

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;


    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;


    /**
     * /chatMemory/chat?message=2加3等于几&conversationId=1001
     * /chatMemory/chat?message=再加8呢&conversationId=1001
     * @param message
     * @param conversationId
     * @return
     */
    @GetMapping("/chatMemory/chat")
    public String chat(@RequestParam("message") String message,
                       @RequestParam("conversationId") String conversationId) {

        return deepseekChatClient.prompt(message)
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
            .call().content();

    }

}
