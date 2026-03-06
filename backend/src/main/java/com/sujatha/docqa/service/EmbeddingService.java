package com.sujatha.docqa.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Service
public class EmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ollamaUrl = "http://localhost:11434/v1/embeddings";
    private final String model = "nomic-embed-text";

    public float[] generateEmbedding(String text) {

        try {
            // Prepare request body
            Map<String, Object> request = Map.of(
                    "model", model,
                    "input", text
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            // Call Ollama
            Map<String, Object> response = restTemplate.postForObject(ollamaUrl, entity, Map.class);

            // Extract embedding from response
            var list = (java.util.List<Map<String, Object>>) response.get("data");
            var embeddingList = (java.util.List<Double>) list.get(0).get("embedding");

            // Convert Double[] to float[]
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