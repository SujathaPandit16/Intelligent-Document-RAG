Data Flow Diagram (Textual)

User → Upload → DocumentController → DocumentService
     → ChunkingService → EmbeddingService → StorageService → Vector DB

User → Query → QueryController → EmbeddingService → QueryService → Vector DB → Chunks → Response

User → Ask → AskController → QueryService → Vector DB → Chunks
     → Prompt Augmentation → Ollama/Gemma → Answer → Response