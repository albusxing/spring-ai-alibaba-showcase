package com.albusxing.showcase.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/5/15
 */
@Configuration
public class LLMConfig {


    /**
     * 方式1:${}
     * yml文件配置：spring.ai.dashscope.api-key=${api-key}
     */
//    @Value("${spring.ai.dashscope.api-key}")
//    private String apiKey;
//
//    @Bean
//    public DashScopeApi dashScopeApi() {
//        return DashScopeApi.builder().apiKey(apiKey).build();
//    }

    /**
     * 方式2:System.getenv("环境变量")
     * yml文件配置：spring.ai.dashscope.api-key=${api-key}
     * @return
     */
//    @Bean
//    public DashScopeApi dashScopeApi() {
//        return DashScopeApi.builder()
//            .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
//            .build();
//    }


    /**
    *  在配置类配置 ChatClient对象
     * @return
     */
//    @Bean
//    public ChatClient chatClient(ChatModel chatModel) {
//        return ChatClient.builder(chatModel).build();
//    }


    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            // 实现 Logger 的 Advisor
            .defaultAdvisors(
                new SimpleLoggerAdvisor()
            )
            // 设置 ChatClient 中 ChatModel 的 Options 参数
            .defaultOptions(
                DashScopeChatOptions.builder()
                    .topP(0.7)
                    .build()
            )
            .build();
    }

}
