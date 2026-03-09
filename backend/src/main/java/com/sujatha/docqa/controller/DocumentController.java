package com.sujatha.docqa.controller;

import com.sujatha.docqa.service.DocumentService;
import com.sujatha.docqa.service.QueryService;
import com.sujatha.docqa.rag.ChunkingService;
import com.sujatha.docqa.storage.DocumentStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ChunkingService chunkingService;

    @Autowired
    private DocumentStorageService storageService;

    @Autowired
    private QueryService queryService;

    // ------------------ Upload Endpoint ------------------
    @PostMapping("/upload")
public String uploadFile(@RequestParam("file") MultipartFile file) {
    try {
        System.out.println("STEP 1: Upload started");

        // ✅ File size guard (5 MB limit)
        if (file.getSize() > 5 * 1024 * 1024) {
            return "Upload failed: File too large. Please upload a document under 5 MB.";
        }

        // Extract text from file
        String content = documentService.extractText(file);
        System.out.println("STEP 2: Text extracted");

        // Split text into chunks
        List<String> chunks = chunkingService.splitTextBySentence(content, 500);
        System.out.println("STEP 3: Chunks created: " + chunks.size());

        // Generate embeddings for each chunk
        List<float[]> embeddings = documentService.generateEmbeddingsForChunks(chunks);

        // Save chunks + embeddings
        storageService.saveChunks(chunks, embeddings);
        System.out.println("STEP 4: Chunks saved");

        return "Document uploaded successfully";
    } catch (Exception e) {
        e.printStackTrace();
        return "Upload failed: " + e.getMessage();
    }
}

    // ------------------ Query Endpoint ------------------
    @PostMapping("/query")
    public List<String> queryDocument(@RequestBody Map<String, Object> payload) {
        try {
        String question = (String) payload.get("question");
        int topN = payload.get("topN") != null ? (int) payload.get("topN") : 5;

        System.out.println("Query received: " + question);

        List<String> results = queryService.getRelevantChunks(question, topN);

        System.out.println("Query results: " + results.size() + " chunks retrieved");
        return results;
        } catch (Exception e) {
        e.printStackTrace();
        return List.of("Query failed: " + e.getMessage());
        }
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestBody Map<String, Object> payload) {
        try {
        String question = (String) payload.get("question");
        int topN = payload.get("topN") != null ? (int) payload.get("topN") : 5;

        System.out.println("Ask received: " + question);

        String answer = queryService.answerQuestion(question, topN);

        System.out.println("Answer generated");
        return answer;
        } catch (Exception e) {
        e.printStackTrace();
        return "Answer failed: " + e.getMessage();
        }
    }
}