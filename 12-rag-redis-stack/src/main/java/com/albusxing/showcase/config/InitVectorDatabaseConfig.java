package com.albusxing.showcase.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Albusxing
 * @created 2026/6/29
 */
@Configuration
public class InitVectorDatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(InitVectorDatabaseConfig.class);
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("classpath:ops.txt")
    private Resource opsFile;


    @PostConstruct
    public void init() {

        //1 读取文件
        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(Charset.defaultCharset());

        //2 文件转换为向量(开启分词)
        List<Document> list = new TokenTextSplitter().transform(textReader.read());

        //3 写入向量数据库RedisStack
        vectorStore.add(list);
        System.out.println("------> 向量初始化数据已经加载过，请不要重复操作");

    }


    @PreDestroy
    public void clear() {
        List<Document> all = vectorStore.similaritySearch(SearchRequest.builder().query("*").topK(1000).build());
        List<String> ids = all.stream().map(Document::getId).toList();
        System.out.println("------> 清空所有数据：ids=" + ids);
        vectorStore.delete(ids);
    }


}
