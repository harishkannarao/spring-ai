# Spring AI
This repository is a playground for learning and experimenting new ideas with AI/LLMs using Spring AI

### Required Software and Tools
* Java Version: 21 (Execute **_java -version_** in command line after installation)
* Ollama: latest (Execute **ollama -v** in command line after installation)
* Integrated Development Environment: Any version IntelliJ Idea or Eclipse

### Build command

    ./mvnw clean install

### Run application with maven

    ./mvnw spring-boot:run

### Run application with java

    ./mvnw clean install -Dmaven.test.skip=true

    java -jar target/spring-ai-0.0.1-SNAPSHOT.jar

### Sample http requests with curl


    curl -X POST "http://localhost:8080/chat-with-context" \
    --header "Content-Type: application/json" \
    --data-binary '{ "context":"My name is Harish", "question":"What is my name?" }'

    curl -X POST 'http://localhost:8080/chat' --data-binary 'Tell me about London, England'

    curl -X GET -G 'http://localhost:8080/books/by-author' --data-urlencode "author=Yuval Noah Harari"

    curl 'http://localhost:8080/stream-joke'

    curl 'http://localhost:8080/joke'