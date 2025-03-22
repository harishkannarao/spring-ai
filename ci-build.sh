#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

docker pull pgvector/pgvector:pg16

./mvnw clean install --batch-mode