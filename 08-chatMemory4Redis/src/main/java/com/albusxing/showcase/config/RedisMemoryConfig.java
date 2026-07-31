package com.albusxing.showcase.config;

import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albusxing
 * @created 2026/6/22
 */
@Configuration
public class RedisMemoryConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;


    @Bean
    public ChatMemoryRepository redisChatMemoryRepository() {
        return JedisRedisChatMemoryRepository.builder()
            .host(host)
            .port(port)
            .build();
    }
}
