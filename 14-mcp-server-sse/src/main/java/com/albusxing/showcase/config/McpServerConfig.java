package com.albusxing.showcase.config;

import com.albusxing.showcase.servcie.OpenMeteoService;
import com.albusxing.showcase.servcie.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/6/29
 */
@Configuration
public class McpServerConfig {



    /**
     * 将工具方法暴露给外部 mcp client 调用
     * @param weatherService
     * @param openMeteoService
     * @return
     */
    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService,
                                             OpenMeteoService openMeteoService) {
        return MethodToolCallbackProvider.builder()
            .toolObjects(weatherService, openMeteoService)
            .build();
    }
}
