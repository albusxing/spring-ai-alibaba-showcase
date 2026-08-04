package com.albusxing.showcase.config;

import com.albusxing.showcase.service.OpenMeteoService;
import com.albusxing.showcase.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/7/31
 */
@Configuration
public class McpConfig {

    /**
     * 注册工具
     */
    @Bean
    public ToolCallbackProvider weatherTools(OpenMeteoService openMeteoService,
                                             WeatherService weatherService) {
        return MethodToolCallbackProvider.builder()
            .toolObjects(openMeteoService, weatherService)
            .build();
    }
}
