package com.sujatha.docqa.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Service
public class EmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ollamaEmbeddingUrl = "http://localhost:11434/api/embeddings"; // correct endpoint
    private final String model = "nomic-embed-text";

    public float[] generateEmbedding(String text) {
        try {
            Map<String, Object> request = Map.of(
                "model", model,
                "prompt", text   // Ollama expects "prompt"
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            Map<String, Object> response =
                restTemplate.postForObject(ollamaEmbeddingUrl, entity, Map.class);

            var embeddingList = (java.util.List<Double>) response.get("embedding");

            float[] embedding = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                embedding[i] = embeddingList.get(i).floatValue();
            }

            return embedding;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No embedding returned from Ollama");
        }
    }
}