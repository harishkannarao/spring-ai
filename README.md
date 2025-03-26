# Spring AI
This repository is a playground for learning and experimenting new ideas with AI/LLMs using Spring AI

### Required Software and Tools
* Java Version: 21 (Execute **_java -version_** in command line after installation)
* Ollama: latest (Execute **ollama -v** in command line after installation)
* Docker: latest (Execute **docker --version** in command line after installation)
* Docker Compose: latest (Execute **docker compose version** in command line after installation)
* Integrated Development Environment: Any version IntelliJ Idea or Eclipse

### Serve and Run Ollama

On Mac/Linux as native app [run_ollama_on_mac.md](https://github.com/harishkannarao/spring-ai/blob/main/run_ollama_on_mac.md)

Using docker [run_ollama_with_docker.md](https://github.com/harishkannarao/spring-ai/blob/main/run_ollama_with_docker.md)

### Start docker dependencies

Pull the dependencies

    docker compose -f compose.yml pull

Start the dependencies

    docker compose -f compose.yml up --build -d

Stop the dependencies

    docker compose -f compose.yml down -v

### Pull Ollama Models

    ollama pull llama3.2:3b

    ollama pull mxbai-embed-large

    ollama pull llava:7b

### Build command

    ./mvnw clean install

### Run application with maven

    ./mvnw clean spring-boot:run

### Run application with maven local configuration against ollama

    ./mvnw clean test-compile exec:java@run-local

### Run application with maven local configuration against AWS AI

    export AWS_AI_ACCESS_KEY_ID={aws_key_id}

    export AWS_AI_SECRET_ACCESS_KEY={aws_secret_key}

    ./mvnw clean test-compile exec:java@run-local-aws

### Run application with java

    ./mvnw clean install -Dmaven.test.skip=true

    java -jar target/spring-ai-0.0.1-SNAPSHOT.jar