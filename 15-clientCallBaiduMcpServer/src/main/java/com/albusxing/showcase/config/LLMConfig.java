package com.albusxing.showcase.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/7/1
 */
@Configuration
public class LLMConfig {


    @Bean
    public ChatClient chatClient(ChatModel chatModel, ToolCallbackProvider tools) {

        ChatClient chatClient = ChatClient.builder(chatModel)
            //mcp协议，配置见yml文件，此处只赋能给ChatClient对象
            .defaultToolCallbacks(tools)
            .build();
        return chatClient;
    }

}
