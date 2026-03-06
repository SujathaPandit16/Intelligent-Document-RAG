package com.sujatha.docqa.service;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private EmbeddingService embeddingService;

    // Extract text from uploaded file
    public String extractText(MultipartFile file) throws Exception {
        Tika tika = new Tika();
        String content = tika.parseToString(file.getInputStream());

        // Optional: just for logging
        System.out.println("File content length: " + content.length());
        return content;
    }

    // New method: generate embeddings for all chunks
    public List<float[]> generateEmbeddingsForChunks(List<String> chunks) throws Exception {
        List<float[]> embeddings = new ArrayList<>();
        for (String chunk : chunks) {
            float[] vector = embeddingService.generateEmbedding(chunk);
            embeddings.add(vector);

            System.out.println("Chunk preview: " + chunk.substring(0, Math.min(50, chunk.length())));
            System.out.println("Embedding size: " + vector.length);
        }
        return embeddings;
    }

    // Optional helper to split text into chunks
    public List<String> splitText(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(text.length(), start + chunkSize);
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }
}