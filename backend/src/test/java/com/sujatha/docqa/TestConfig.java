package com.sujatha.docqa;

import com.sujatha.docqa.service.EmbeddingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public EmbeddingService embeddingService() {
        return new EmbeddingService() {
            @Override
            public float[] generateEmbedding(String text) {
                return new float[512]; // dummy embedding
            }
        };
    }
}