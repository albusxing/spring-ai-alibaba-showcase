package com.albusxing.showcase.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/6/29
 */
@Configuration
public class LLMConfig {


    /**
     * ToolCallbackProvider 提供工具回调
     * @param chatModel
     * @param tools
     * @return
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel, ToolCallbackProvider tools) {
        return ChatClient.builder(chatModel)
            .defaultToolCallbacks(tools.getToolCallbacks())
            .build();
    }
}
