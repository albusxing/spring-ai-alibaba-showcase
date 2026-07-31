package com.albusxing.showcase.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/5/15
 */
@Configuration
public class LLMConfig {

    // 模型名称常量定义
    private final String QWEN_MODEL = "qwen-plus";

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean(name = "qwen")
    public ChatModel qwen() {
        return DashScopeChatModel.builder()
            .dashScopeApi(
                DashScopeApi.builder()
                    .apiKey(apiKey)
                    .build())
            .defaultOptions(
                DashScopeChatOptions.builder().model(QWEN_MODEL).build()
            ).build();
    }

    @Bean(name = "inMemoryChatMemoryRepository")
    public ChatMemoryRepository inMemoryChatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }


    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwen") ChatModel chatModel,
                                     ChatMemoryRepository inMemoryChatMemoryRepository) {

        // add MessageWindowChatMemory
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
            .chatMemoryRepository(inMemoryChatMemoryRepository)
            .maxMessages(100)
            .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder().model(QWEN_MODEL).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build())
                .build();
    }


}
