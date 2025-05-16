#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

docker --version

docker pull pgvector/pgvector:pg16

curl -fsSL https://ollama.com/install.sh | sh

ollama --version

ollama serve &

curl --retry 300 --retry-delay 1 --retry-all-errors "http://localhost:11434"

ollama pull llama3.2:3b

ollama pull mxbai-embed-large

ollama pull llava:7b