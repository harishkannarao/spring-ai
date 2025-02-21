# Spring AI
This repository is a playground for learning and experimenting new ideas with AI/LLMs using Spring AI

### Required Software and Tools
* Java Version: 21 (Execute **_java -version_** in command line after installation)
* Ollama: latest (Execute **ollama -v** in command line after installation)
* Integrated Development Environment: Any version IntelliJ Idea or Eclipse

### Serve and Run Ollama

In first terminal execute the command and leave it running, press `Ctrl+C` to stop ollama engine

    ollama serve

In second terminal execute the command and leave it running, type `/bye` to exit and stop the LLM

    ollama run deepseek-r1:8b

To run LLM in non-interactive mode and keep it alive for 60 minutes, in the second terminal execute the command

    ollama run deepseek-r1:8b "Hi" --keepalive 60m

To run LLM in non-interactive mode and keep it forever, in the second terminal execute the command

    ollama run deepseek-r1:8b "Hi" --keepalive -1m

To explicitly stop an LLM running in non-interactive mode

    ollama stop deepseek-r1:8b

### Start docker dependencies

Pull the dependencies

    docker compose -f compose.yml pull

Start the dependencies

    docker compose -f compose.yml up --build -d

Stop the dependencies

    docker compose -f compose.yml down -v

### Build command

    ./mvnw clean install

### Run application with maven

    ./mvnw spring-boot:run

### Run application with java

    ./mvnw clean install -Dmaven.test.skip=true

    java -jar target/spring-ai-0.0.1-SNAPSHOT.jar