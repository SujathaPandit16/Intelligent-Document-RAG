package com.sujatha.docqa.controller;

import com.sujatha.docqa.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    @Autowired
    private QueryService queryService;

    /**
     * Existing endpoint: returns top N relevant chunks only
     * Example POST JSON: {"question":"What is AI?", "topN":3}
     */
    @PostMapping
    public List<String> askQuestion(@RequestBody QueryRequest request) {
        return queryService.getRelevantChunks(request.getQuestion(), request.getTopN());
    }

    /**
     * ✅ New endpoint: returns full LLM answer using Ollama
     * Example POST JSON: {"question":"What is AI?", "topN":3}
     */
    @PostMapping("/answer")
    public String getAnswer(@RequestBody QueryRequest request) {
        return queryService.answerQuestion(request.getQuestion(), request.getTopN());
    }

    // Request body class stays the same
    public static class QueryRequest {
        private String question;
        private int topN;

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public int getTopN() { return topN; }
        public void setTopN(int topN) { this.topN = topN; }
    }
}