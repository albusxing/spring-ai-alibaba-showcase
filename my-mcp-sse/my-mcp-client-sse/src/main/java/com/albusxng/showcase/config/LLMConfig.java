package com.albusxng.showcase.config;

import org.springframework.ai.chat.client.ChatClient;
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
     * @param chatClientBuilder
     * @param tools
     * @return
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools) {
        return chatClientBuilder
            .defaultToolCallbacks(tools.getToolCallbacks())
            .build();
    }
}
