package com.sujatha.docqa.storage;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DocumentStorageService {

    // Map to store chunk text -> embedding
    private final Map<String, float[]> chunkEmbeddings = new HashMap<>();

    // Save chunks with embeddings
    public void saveChunks(List<String> chunks, List<float[]> embeddings) {
        for (int i = 0; i < chunks.size(); i++) {
            chunkEmbeddings.put(chunks.get(i), embeddings.get(i));
        }
    }

    // Retrieve all stored chunks with embeddings
    public Map<String, float[]> getAllChunksWithEmbeddings() {
        return Collections.unmodifiableMap(chunkEmbeddings);
    }
}