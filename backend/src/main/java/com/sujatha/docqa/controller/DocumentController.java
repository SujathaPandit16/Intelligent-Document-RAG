package com.sujatha.docqa.controller;

import com.sujatha.docqa.service.DocumentService;
import com.sujatha.docqa.service.QueryService;
import com.sujatha.docqa.rag.ChunkingService;
import com.sujatha.docqa.storage.DocumentStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

            // Extract text from file
            String content = documentService.extractText(file);
            System.out.println("STEP 2: Text extracted");

            // Split text into chunks
            List<String> chunks = chunkingService.splitText(content, 500);
            System.out.println("STEP 3: Chunks created: " + chunks.size());

            // Generate embeddings for each chunk
            List<float[]> embeddings = documentService.generateEmbeddingsForChunks(chunks);

            // Save chunks + embeddings
            storageService.saveChunks(chunks, embeddings);
            System.out.println("STEP 4: Chunks saved");

            return "Document uploaded successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Upload failed";
        }
    }

    // ------------------ Query Endpoint ------------------
    @PostMapping("/query")
    public List<String> queryDocument(@RequestParam("question") String question,
                                      @RequestParam(value = "topN", defaultValue = "5") int topN) {
        try {
            System.out.println("Query received: " + question);

            // Retrieve top N relevant chunks
            List<String> results = queryService.getRelevantChunks(question, topN);

            System.out.println("Query results: " + results.size() + " chunks retrieved");
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Query failed: " + e.getMessage());
        }
    }
}