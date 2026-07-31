package com.albusxing.showcase.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/6/29
 */
@Configuration
public class LLMConfig {


    @Bean
    public ChatClient qwenChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
