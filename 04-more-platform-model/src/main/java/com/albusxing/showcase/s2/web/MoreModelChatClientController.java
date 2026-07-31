package com.albusxing.showcase.s2.web;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Set;


/**
 * @author Albusxing
 * @created 2026/7/24
 */
@RestController
@RequestMapping("/more-model-chat-client")
public class MoreModelChatClientController {

    private final Set<String> modelList = Set.of(
        "deepseek-r1",
        "deepseek-v3",
        "qwen-plus",
        "qwen-max"
    );

    private final ChatClient chatClient;

    public MoreModelChatClientController(@Qualifier("dashScopeChatModel") ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }



    @GetMapping("/{model}/{prompt}")
    public Flux<String> streamModels(@PathVariable String model, @PathVariable String prompt) {

        if (!modelList.contains(model)) {

            return Flux.just("model not exist");
        }

        System.out.println("===============================================");
        System.out.println("当前输入的模型为：" + model);
        System.out.println("默认模型为：" + DashScopeModel.ChatModel.QWEN_PLUS.getValue());
        System.out.println("===============================================");

        return chatClient.prompt()
            .user(prompt)
            .options(DashScopeChatOptions.builder()
                .model(model)
                .build()
            )
            .stream().content();
    }
}
