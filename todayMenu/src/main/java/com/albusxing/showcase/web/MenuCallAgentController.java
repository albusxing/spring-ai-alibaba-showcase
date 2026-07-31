package com.albusxing.showcase.web;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Albusxing
 * @created 2026/7/1
 */
@RestController
@RequestMapping("/menu")
public class MenuCallAgentController {

    // 百炼平台的appid
    @Value("${spring.ai.dashscope.agent.options.app-id}")
    private String appId;

    // 百炼云平台的智能体接口对象
    private DashScopeAgent dashScopeAgent;

    public MenuCallAgentController(DashScopeAgentApi dashScopeAgentApi) {
        this.dashScopeAgent = new DashScopeAgent(dashScopeAgentApi);
    }


    /**
     * 对接百炼智能体工作流应用
     * @param msg
     * @return
     */
    @GetMapping(value = "/eatAgent")
    public String eatAgent(@RequestParam(name = "msg",defaultValue = "今天吃什么") String msg) {

        DashScopeAgentOptions dashScopeAgentOptions = DashScopeAgentOptions.builder()
            // 百炼智能体工作流应用 appId
            .appId(appId)
            .build();

        Prompt prompt = new Prompt(msg, dashScopeAgentOptions);

        return dashScopeAgent.call(prompt).getResult().getOutput().getText();
    }

}
