package com.albusxing.showcase.web;

import com.alibaba.cloud.ai.advisor.DocumentRetrievalAdvisor;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Albusxing
 * @created 2026/7/1
 */
@RestController
@RequestMapping("/rag-bailian-knowledge")
public class RagBailianKnowledgeController {

    @Resource
    private ChatClient chatClient;
    @Resource
    private DashScopeApi dashScopeApi;


    /**
     * /chat
     * /chat?msg=A0001
     * @param msg
     * @return
     */
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "00000错误信息") String msg) {

        // 百炼文档检索器
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
            DashScopeDocumentRetrieverOptions.builder()
                // 百炼知识库名称
                .indexName("ops")
                .build()
        );

        return chatClient.prompt()
            .user(msg)
            .advisors(new DocumentRetrievalAdvisor(retriever))
            .stream()
            .content();
    }
}
