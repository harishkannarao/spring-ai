# Run Ollama and LLMs on Mac/Linux using native app

These commands are useful to start and run ollama on Mac/Linux developer machines using native app.

In first terminal execute the command and leave it running, press `Ctrl+C` to stop ollama engine

    ollama serve

In second terminal, pull the LLM model

    ollama pull deepseek-r1:8b

In second terminal, pull the embedding model 

    ollama pull mxbai-embed-large

In second terminal execute the command and leave it running, type `/bye` to exit and stop the LLM

    ollama run deepseek-r1:8b

To run LLM in non-interactive mode and keep it alive for 60 minutes, in the second terminal execute the command

    ollama run deepseek-r1:8b "Hi" --keepalive 60m

To run LLM in non-interactive mode and keep it forever, in the second terminal execute the command

    ollama run deepseek-r1:8b "Hi" --keepalive -1m

To explicitly stop an LLM running in non-interactive mode

    ollama stop deepseek-r1:8b