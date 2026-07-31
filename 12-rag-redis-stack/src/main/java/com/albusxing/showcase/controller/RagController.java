package com.albusxing.showcase.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html#_advanced_rag
 *
 * @author Albusxing
 * @created 2026/6/29
 */
@RestController
public class RagController {

    @Resource
    private ChatClient chatClient;
    @Resource
    private VectorStore vectorStore;


    /**
     * /rag/redis?msg=00000
     * /rag/redis?msg=C2222
     * @param msg
     * @return
     */
    @GetMapping("/rag/redis")
    public Flux<String> rag(@RequestParam("msg") String msg) {

        String systemInfo = """
            你是一个运维工程师,按照给出的编码给出对应故障解释,否则回复找不到信息。
            """;


        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
            .documentRetriever(
                VectorStoreDocumentRetriever.builder()
                    .vectorStore(vectorStore)
                    .build()
            ).build();

        return chatClient.prompt()
            .system(systemInfo)
            .user(msg)
            .advisors(advisor)
            .stream().content();
    }

}
