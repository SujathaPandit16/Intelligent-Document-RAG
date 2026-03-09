# Retrieval-Augmented Generation (RAG) with Spring Boot + Ollama

This project demonstrates an end-to-end **Retrieval-Augmented Generation (RAG)** pipeline using **Java + Spring Boot** for backend APIs, **Ollama (Gemma model)** for text generation, and a simple vector storage for embeddings.

## 🚀 Features
- 📂 **Document Upload**: Upload text/PDF files, extract content, and split into chunks.
- 🔎 **Query API**: Retrieve the most relevant chunks using semantic similarity.
- 💬 **Ask API**: Generate grounded answers by augmenting user queries with retrieved chunks.
- ⚡ **Chunking Service**: Sentence-based splitting with word boundary safety.
- 🛡️ **File Size Guard**: Prevents uploads larger than 5 MB to avoid memory issues.

## 🏗️ Architecture
1. **Upload** → Extract text → Chunk → Generate embeddings → Store in vector DB  
2. **Query** → Embed question → Retrieve topN relevant chunks  
3. **Ask** → Augment prompt with chunks → Generate answer via Ollama (Gemma)

## 🛠️ Tech Stack
- **Java 17 + Spring Boot**
- **Ollama (Gemma 2B model)**
- **Vector storage service** (custom implementation)
- **Postman** for API testing
