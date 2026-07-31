package com.albusxing.showcase.controller;

import com.albusxing.showcase.model.StudentRecord;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;


/**
 * @author Albusxing
 * @created 2026/6/22
 */
@RestController
public class StructuredOutputController {

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;


    /**
     * /structuredoutput/chat?name=李四&email=lgq@gmail.com
     * 函数式编程
     *
     * @param name
     * @return
     */
    @GetMapping("/structuredOutput/chat")
    public StudentRecord chat(@RequestParam(name = "name") String name,
                              @RequestParam(name = "email") String email) {

        return qwenChatClient.prompt().user(new Consumer<>() {
            @Override
            public void accept(ChatClient.PromptUserSpec promptUserSpec) {
                promptUserSpec.text("学号1001，我叫{name},大学专业计算机科学与技术,邮箱{email}")
                    .param("name", name)
                    .param("email", email);
            }
        }).call().entity(StudentRecord.class);
    }


    /**
     * /structuredOutput/chat2?name=孙伟&email=lgq@gmail.com
     */
    @GetMapping("/structuredOutput/chat2")
    public StudentRecord chat2(@RequestParam(name = "name") String name,
                               @RequestParam(name = "email") String email) {

        String stringTemplate = "学号1002，我叫{name},大学专业软件工程,邮箱{email}";

        return qwenChatClient.prompt()
            .user(promptUserSpec -> promptUserSpec.text(stringTemplate)
                .param("name", name)
                .param("email", email))
            .call()
            .entity(StudentRecord.class);
    }

}
