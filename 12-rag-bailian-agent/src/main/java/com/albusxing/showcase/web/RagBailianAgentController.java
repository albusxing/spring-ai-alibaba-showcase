package com.albusxing.showcase.web;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/7/7
 */
@RestController
@RequestMapping("/rag-bailian-agent")
public class RagBailianAgentController {


    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;

    /**
     * 使用 DashScopeAgent 调用 DashScope API 进行对话
     */
    private final DashScopeAgent dashScopeAgent;

    public RagBailianAgentController(DashScopeAgentApi dashScopeAgentApi) {
        this.dashScopeAgent = new DashScopeAgent(dashScopeAgentApi);
    }


    /**
     * 西红市实验十小一年二班的班主任是谁？
      * @param message
     * @return
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message") String message) {

        ChatResponse chatResponse = dashScopeAgent.call(new Prompt(message,
            DashScopeAgentOptions.builder()
                .appId(appId)
                .build()));
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * 流式输出
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(value = "message") String message) {
        return dashScopeAgent.stream(
                new Prompt(
                    message,
                    DashScopeAgentOptions.builder().appId(appId).build()
                    )
                ).map(response -> {
                    AssistantMessage app_output = response.getResult().getOutput();
                return app_output.getText();
            });
    }
}
