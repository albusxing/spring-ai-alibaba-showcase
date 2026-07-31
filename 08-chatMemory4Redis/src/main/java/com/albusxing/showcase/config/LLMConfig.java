package com.albusxing.showcase.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
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
    private final String DEEPSEEK_MODEL = "deepseek-v3";
    private final String QWEN_MODEL = "qwen-plus";

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean(name = "deepseek")
    public ChatModel deepSeek() {

        return DashScopeChatModel.builder()
            .dashScopeApi(
                DashScopeApi.builder()
                    .apiKey(apiKey)
                    .build())
            .defaultOptions(
                DashScopeChatOptions.builder().model(DEEPSEEK_MODEL).build()
            ).build();
    }


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



    /**
     * 为 deepseekChatClient 添加 redis 对话记忆
     * @param deepSeek
     * @param chatMemoryRepository
     * @return
     */
    @Bean(name = "deepseekChatClient")
    public ChatClient deepseekChatClient(@Qualifier("deepseek") ChatModel deepSeek,
                                         ChatMemoryRepository chatMemoryRepository) {
        // add MessageWindowChatMemory
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
            .chatMemoryRepository(chatMemoryRepository)
            .maxMessages(100)
            .build();


        return ChatClient.builder(deepSeek)
                .defaultOptions(
                    ChatOptions.builder().model(DEEPSEEK_MODEL).build())
                // add advisors
                .defaultAdvisors(
                    MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build()
                ).build();
    }


    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwen") ChatModel qwen) {
        return ChatClient.builder(qwen)
                .defaultOptions(
                    ChatOptions.builder()
                    .model(QWEN_MODEL)
                    .build()
                ).build();
    }


}
