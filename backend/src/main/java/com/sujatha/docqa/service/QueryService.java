package com.sujatha.docqa.service;

import com.sujatha.docqa.storage.DocumentStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QueryService {

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private DocumentStorageService storageService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ollamaUrl = "http://localhost:11434/api/generate";


    /**
     * Retrieve top N relevant chunks for a user question using cosine similarity.
     * Exceptions in embedding generation are caught inside the method.
     */
    public List<String> getRelevantChunks(String question, int topN) {

        try {
            // 1️⃣ Generate embedding for the question
            float[] questionVector = embeddingService.generateEmbedding(question);

            // 2️⃣ Fetch all stored chunks with their embeddings
            Map<String, float[]> chunkMap = storageService.getAllChunksWithEmbeddings();

            // 3️⃣ Compute cosine similarity for each chunk
            List<Map.Entry<String, Float>> scoredChunks = new ArrayList<>();
            for (Map.Entry<String, float[]> entry : chunkMap.entrySet()) {
                float score = cosineSimilarity(questionVector, entry.getValue());
                scoredChunks.add(Map.entry(entry.getKey(), score));
            }

            // 4️⃣ Sort by similarity descending and pick top N
            return scoredChunks.stream()
                    .sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
                    .limit(topN)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Log the error
            System.err.println("Failed to generate query embedding: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list if something fails
        }
    }

    // New method: call Ollama with context
    public String answerQuestion(String question, int topN) {
    List<String> contextChunks = getRelevantChunks(question, topN);

    String prompt = "Answer the following question using the provided context.\n\n"
            + String.join("\n", contextChunks)
            + "\n\nQuestion: " + question;

    Map<String, Object> request = Map.of(
        "model", "gemma:2b",   // use the pulled model
        "prompt", prompt,
        "options", Map.of("num_predict", 200)
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

    // Use RestTemplate.exchange to stream the response
    ResponseEntity<String> response =
        restTemplate.postForEntity(ollamaUrl, entity, String.class);

    // Ollama returns multiple JSON objects separated by newlines
    String[] lines = response.getBody().split("\n");

    StringBuilder answer = new StringBuilder();
    for (String line : lines) {
        try {
            // Parse each line as JSON
            Map<String, Object> obj = new ObjectMapper().readValue(line, Map.class);
            if (obj.containsKey("response")) {
                answer.append(obj.get("response"));
            }
        } catch (Exception e) {
            // skip malformed lines
        }
    }

    return answer.toString().trim();
}
    /**
     * Cosine similarity helper function.
     */
    private float cosineSimilarity(float[] vec1, float[] vec2) {
        if (vec1.length != vec2.length) return 0f;
        float dot = 0f;
        float norm1 = 0f;
        float norm2 = 0f;
        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }
        return dot / ((float) (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10));
    }
}