package com.albusxing.showcase.web;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/6/29
 */
@RestController
@RequestMapping("/mcp-client-sse")
public class McpClientSseController {

    @Resource
    private ChatClient chatClient;//使用mcp支持

    @Resource
    private ChatModel chatModel;//没有纳入tool支持，普通调用


    /**
     * /mcp-client-sse/chat?msg=上海天气 ==》 调用 WeatherService.getWeatherByCity("上海")
     * /mcp-client-sse/chat?msg=纬度39.9，经度116.4的天气预报 ==》调用 OpenMeteoService.getWeatherForecastByLocation(39.9, 116.4)
     * @param msg
     * @return
     */
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "北京") String msg) {
        System.out.println("使用了mcp");
        return chatClient.prompt(msg).stream().content();
    }

    @RequestMapping("/chat2")
    public Flux<String> chat2(@RequestParam(name = "msg", defaultValue = "北京") String msg) {
        System.out.println("未使用mcp");
        return chatModel.stream(msg);
    }
}
