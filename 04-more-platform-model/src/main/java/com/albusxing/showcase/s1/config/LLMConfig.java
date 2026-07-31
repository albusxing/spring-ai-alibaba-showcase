package com.albusxing.showcase.s1.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多模型共存
 * @author Albusxing
 * @created 2026/7/6
 */
@Configuration
public class LLMConfig {

    // 模型名称常量定义，一套系统多模型共存
    private final String DEEPSEEK_MODEL = "deepseek-v3";
    private final String QWEN_MODEL = "qwen-max";

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean(name = "deepSeekChatModel")
    public ChatModel deepSeekChatModel() {

        return DashScopeChatModel.builder()
            .dashScopeApi(
                DashScopeApi.builder()
                    .apiKey(apiKey)
                    .build())
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .model(DEEPSEEK_MODEL)
                    .build())
            .build();
    }

    @Bean(name = "qwenChatModel")
    public ChatModel qwenChatModel() {

        return DashScopeChatModel.builder()
            .dashScopeApi(
                DashScopeApi.builder()
                .apiKey(apiKey)
                .build())
            .defaultOptions(
                DashScopeChatOptions.builder()
                .model(QWEN_MODEL)
                .build())
            .build();
    }


    @Bean(name = "deepSeekChatClient")
    public ChatClient deepSeekChatClient(ChatModel deepSeekChatModel) {

        return ChatClient.builder(deepSeekChatModel)
            .defaultOptions(DashScopeChatOptions.builder()
                .model(DEEPSEEK_MODEL)
                .build())
            .build();
    }

    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(ChatModel qwenChatModel) {

        return ChatClient.builder(qwenChatModel)
            .defaultOptions(DashScopeChatOptions.builder()
                .model(QWEN_MODEL)
                .build())
            .build();
    }

}
