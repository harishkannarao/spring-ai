# Run Ollama and LLMs with docker

These commands are useful to start and run ollama with docker.
This is especially handy while working on Linux machines or CI systems.

### Pre-requisite

Verify installation of docker engine and docker client using the commands

    docker --version

    docker compose version

### Start Ollama engine with docker

    docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama-engine ollama/ollama

### Run a LLM with interactive mode

    docker exec -it ollama-engine ollama run llama3.2:3b

### Run a LLM with non-interactive mode and keep running the model forever

    docker exec ollama-engine ollama run llama3.2:3b "Hi" --keepalive -1m

### Stop a LLM with non-interactive mode

    docker exec ollama-engine ollama stop llama3.2:3b

### Stop Ollama engine

    docker stop ollama-engine