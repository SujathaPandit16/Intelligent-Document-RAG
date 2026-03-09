package com.sujatha.docqa.rag;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {

    public List<String> splitTextBySentence(String text, int chunkSize) {
    List<String> chunks = new ArrayList<>();
    String[] sentences = text.split("(?<=[.!?])\\s+"); // split by sentence boundaries

    StringBuilder currentChunk = new StringBuilder();
    for (String sentence : sentences) {
        if (currentChunk.length() + sentence.length() > chunkSize) {
            chunks.add(currentChunk.toString().trim());
            currentChunk.setLength(0);
        }
        currentChunk.append(sentence).append(" ");
    }
    if (currentChunk.length() > 0) {
        chunks.add(currentChunk.toString().trim());
    }
    return chunks;
}
}