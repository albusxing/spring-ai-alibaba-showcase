package com.albusxing.showcase.web;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
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
@RequestMapping("/rag4Simple")
public class Rag4SimpleController {


    @Resource
    private ChatClient chatClient;
    @Resource
    private VectorStore vectorStore;


    /**
     *  /chat?userInput=机器人有哪些功能？
     * @param msg
     * @return
     */
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam("msg") String msg) {

        return chatClient.prompt()
            .user(msg)
            .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
            .stream()
            .content();
    }
}
