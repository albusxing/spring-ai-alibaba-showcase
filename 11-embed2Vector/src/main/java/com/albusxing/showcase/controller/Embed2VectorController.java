package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Embed2VectorController {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private VectorStore redisVectorStore;


    /**
     * 文本向量化
     * http://localhost:8011/text2embed?msg=射雕英雄传
     *
     * @param msg
     * @return
     */
    @GetMapping("/text2Embed")
    public EmbeddingResponse text2Embed(@RequestParam("msg") String msg) {

        EmbeddingResponse embeddingResponse = embeddingModel.call(
                                new EmbeddingRequest(List.of(msg),
                                    DashScopeEmbeddingOptions.builder().model("text-embedding-v3").build())
                                );

        System.out.println(Arrays.toString(embeddingResponse.getResult().getOutput()));

        return embeddingResponse;
    }

    /**
     * 文本向量化 后存入向量数据库RedisStack
     */
    @GetMapping("/embed2vector/add")
    public void add() {
        List<Document> documents = List.of(
            new Document("i study LLM"),
            new Document("i love java")
        );

        redisVectorStore.add(documents);
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

        List<Document> list = redisVectorStore.similaritySearch(searchRequest);

        System.out.println(list);

        return list;
    }


    @GetMapping("/import")
    public void importData() {
        System.out.println("start import data");

        HashMap<String, Object> map = new HashMap<>();
        // 增加其他元数据，结构如下：
        /**
         * {
         *   "year": "2025",
         *   "name": "liguoqing",
         *   "embedding": [
         *     -0.017181735,
         *     0.048420377,
         *   ],
         *   "id": "12345",
         *   "content": "Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!"
         * }
         */
        map.put("id", "12345");
        map.put("year", "2025");
        map.put("name", "lgq");
        List<Document> documents = List.of(
            new Document("The World is Big and Salvation Lurks Around the Corner"),
            new Document("You walk forward facing the past and you turn back toward the future.", Map.of("year", 2024)),
            new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", map));
        redisVectorStore.add(documents);
    }


    @GetMapping("/search")
    public List<Document> search() {
        System.out.println("start search data");
        return redisVectorStore.similaritySearch(SearchRequest
            .builder()
            .query("Spring")
            .topK(2)
            .build());
    }


    @GetMapping("/delete-filter")
    public void deleteFilter() {
        System.out.println("start delete data with filter");
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.eq("name", "lgq").build();

        redisVectorStore.delete(expression);
    }
}
