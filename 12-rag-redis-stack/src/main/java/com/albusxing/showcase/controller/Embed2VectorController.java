package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
public class Embed2VectorController {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private VectorStore vectorStore;


    /**
     * 文本向量化
     * http://localhost:8011/text2embed?msg=射雕英雄传
     *
     * @param msg
     * @return
     */
    @GetMapping("/text2Embed")
    public EmbeddingResponse text2Embed(@RequestParam("msg") String msg) {

        EmbeddingRequest embeddingRequest = new EmbeddingRequest(List.of(msg),
                            DashScopeEmbeddingOptions.builder().model("text-embedding-v3").build());

        EmbeddingResponse embeddingResponse = embeddingModel.call(embeddingRequest);

        System.out.println(Arrays.toString(embeddingResponse.getResult().getOutput()));

        return embeddingResponse;
    }

    /**
     * 文本向量化 后存入向量数据库RedisStack
     */
    @GetMapping("/embed2Vector/add")
    public void add() {
        List<Document> documents = List.of(
            new Document("I study LLM"),
            new Document("I love java")
        );

        vectorStore.add(documents);
    }

    /**
     * 从向量数据库RedisStack查找，进行相似度查找
     * http://localhost:8011/embed2vector/get?msg=LLM
     *
     * @param msg
     * @return
     */
    @GetMapping("/embed2vector/get")
    public List<?> getAll(@RequestParam(name = "msg") String msg) {
        SearchRequest searchRequest = SearchRequest.builder()
            .query(msg)
            .topK(2)
            .build();

        List<Document> list = vectorStore.similaritySearch(searchRequest);

        System.out.println(list);

        return list;
    }
}
