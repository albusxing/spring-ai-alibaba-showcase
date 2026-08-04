package com.albusxng.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/7/31
 */
@RestController
@RequestMapping("/mcp-client")
public class SseMcpClientController {

    /**
     * 使用mcp支持
     */
    @Resource
    private ChatClient chatClient;


    /**
     * /mcp-client-sse/chat?msg=上海天气 ==》 调用 WeatherService.getWeatherByCity("上海")
     * /mcp-client-sse/chat?msg=纬度39.9，经度116.4的天气预报
     *              ==》调用 OpenMeteoService.getWeatherForecastByLocation(39.9, 116.4)
     *
     * @param userInput
     * @return
     */
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam("userInput") String userInput) {
        System.out.println("使用了mcp");
        return chatClient.prompt(userInput).stream().content();
    }
}
