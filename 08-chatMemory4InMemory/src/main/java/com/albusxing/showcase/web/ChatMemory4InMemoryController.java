package com.albusxing.showcase.web;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Albusxing
 * @created 2026/7/6
 */
@RestController
public class ChatMemory4InMemoryController {

    @Resource
    private ChatClient chatClient;

    @GetMapping("/chatMemory/chat")
    public String chat(@RequestParam("message") String message,
                       @RequestParam("conversationId") String conversationId) {

        return chatClient.prompt(message)
            .advisors(advisorSpec ->
                advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)).call().content();

    }


//    @GetMapping("/messages")
//    public List<Message> messages(@RequestParam(value = "conversation_id", defaultValue = "yingzi") String conversationId) {
//        return messageWindowChatMemory.get(conversationId);
//    }
}
