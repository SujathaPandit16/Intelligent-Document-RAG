Purpose: Installation and running instructions.
Content to include:
- Prerequisites (Java, Maven, Ollama, Gemma model).

# Setup Guide
## Prerequisites
- Java 17+
- Maven
- Ollama installed locally
- Gemma model pulled:
  ```bash
  ollama pull gemma:2b


- How to clone and build the project.
git clone https://github.com/yourusername/rag-spring-boot.git
cd rag-spring-boot
mvn clean install


- How to run (mvn spring-boot:run).
mvn spring-boot:run


- JVM heap size notes (-Xmx2g).
For larger documents:
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx2g"


- File size limit (5 MB).
- Max upload size: 5 MB
- Larger files are rejected to prevent memory issues

