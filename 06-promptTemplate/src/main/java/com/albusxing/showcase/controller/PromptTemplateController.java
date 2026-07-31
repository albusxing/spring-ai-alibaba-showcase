package com.albusxing.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * @author Albusxing
 * @created 2026/6/22
 * https://java2ai.com/docs/1.0.0.2/tutorials/basics/prompt/?spm=5176.29160081.0.0.2856aa5cdeol7a
 *
 */
@RestController
public class PromptTemplateController {

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;


    @Value("classpath:/promptTemplate/my-template.txt")
    private org.springframework.core.io.Resource userTemplate;

    /**
     * PromptTemplate基本使用，使用占位符设置模版 PromptTemplate
     * /promptTemplate/chat?topic=java&output_format=html&wordCount=200
     *
     * @param topic
     * @param output_format
     * @param wordCount
     * @return
     */
    @GetMapping("/promptTemplate/chat")
    public Flux<String> chat(String topic, String output_format, String wordCount) {
        PromptTemplate promptTemplate = new PromptTemplate("" +
            "讲一个关于{topic}的故事" +
            "并以{output_format}格式输出，" +
            "字数在{wordCount}左右");

        // PromptTempate -> Prompt
        Prompt prompt = promptTemplate.create(Map.of(
            "topic", topic,
            "output_format", output_format,
            "wordCount", wordCount));

        return deepseekChatClient.prompt(prompt).stream().content();
    }


    /**
     * PromptTemplate读取模版文件实现模版功能
     * /promptTemplate/chat2?topic=java&output_format=html
     */
    @GetMapping("/promptTemplate/chat2")
    public String chat2(String topic, String output_format) {
        PromptTemplate promptTemplate = new PromptTemplate(userTemplate);

        Prompt prompt = promptTemplate.create(Map.of("topic", topic, "output_format", output_format));

        return deepseekChatClient.prompt(prompt).call().content();
    }


    /**
     * 系统消息(SystemMessage)：设定AI的行为规则和功能边界(xxx助手/什么格式返回/字数控制多少)。
     * 用户消息(UserMessage)：用户的提问/主题
     * /promptTemplate/chat3?sysTopic=法律&userTopic=知识产权法
     * /promptTemplate/chat3?sysTopic=法律&userTopic=夫妻肺片
     */
    @GetMapping("/promptTemplate/chat3")
    public String chat3(String sysTopic, String userTopic) {
        // 1.SystemPromptTemplate
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("你是{systemTopic}助手，只回答{systemTopic}相关问题，" +
            "其它无可奉告，以HTML格式的结果。");
        Message sysMessage = systemPromptTemplate.createMessage(Map.of("systemTopic", sysTopic));
        // 2.PromptTemplate
        PromptTemplate userPromptTemplate = new PromptTemplate("解释一下{userTopic}");
        Message userMessage = userPromptTemplate.createMessage(Map.of("userTopic", userTopic));

        // 3.组合【关键】 多个 Message -> Prompt
        Prompt prompt = new Prompt(List.of(sysMessage, userMessage));
        // 4.调用 LLM
        return deepseekChatClient.prompt(prompt).call().content();
    }

    /**
     * 人物角色设定，通过SystemMessage来实现人物设定，本案例用ChatModel实现
     * 设定AI为”医疗专家”时，仅回答医学相关问题
     * 设定AI为编程助手”时，专注于技术问题解答
     * /promptTemplate/chat4?question=牡丹花
     */
    @GetMapping("/promptTemplate/chat4")
    public String chat4(String question) {
        //1 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个Java编程助手，拒绝回答非技术问题。");
        //2 用户消息
        UserMessage userMessage = new UserMessage(question);
        //3 系统消息+用户消息=完整提示词
        //Prompt prompt = new Prompt(systemMessage, userMessage);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        //4 调用LLM
        return deepseekChatModel.call(prompt).getResult().getOutput().getText();
    }


    /**
     * 人物角色设定，通过SystemMessage来实现人物设定，本案例用ChatClient实现
     * 设定AI为"医疗专家"时，仅回答医学相关问题
     * 设定AI为"编程助手"时，专注于技术问题解答
     * promptTemplate/chat5?question=火锅
     */
    @GetMapping("/promptTemplate/chat5")
    public Flux<String> chat5(String question) {
        return deepseekChatClient.prompt()
            .system("你是一个Java编程助手，拒绝回答非技术问题。")
            .user(question)
            .stream()
            .content();
    }
}
