### Sample http requests with curl

    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/spring-ai/simple-chat" -F "input=Tell me about London, England"
    
    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/spring-ai/simple-stream-chat" -F "input=Tell me about London, England"

```
curl -X POST "http://localhost:8080/spring-ai/chat-with-context" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
{
    "context":"My name is Harish",
    "question":"What is my name?" 
}
EOF
```

```
curl -X POST "http://localhost:8080/spring-ai/chat-with-memory" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
{
    "chat":"My name is Harish",
    "conversationId":"D35D3365-4F45-4EE2-900D-67B5D8563EFC" 
}
EOF
```

```
curl -X DELETE "http://localhost:8080/spring-ai/delete-conversation-id" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
{
    "conversationId":"D35D3365-4F45-4EE2-900D-67B5D8563EFC" 
}
EOF
```

```
curl -X POST "http://localhost:8080/spring-ai/chat-with-memory" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
{
    "chat":"My name is Harish"
}
EOF
```

```
curl -X POST "http://localhost:8080/spring-ai/chat-with-memory" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
{
    "chat":"What is my name?",
    "conversationId":"D35D3365-4F45-4EE2-900D-67B5D8563EFC" 
}
EOF
```

    curl -X GET -G 'http://localhost:8080/spring-ai/books/by-author' --data-urlencode "author=Yuval Noah Harari"

    curl 'http://localhost:8080/spring-ai/stream-joke'

    curl 'http://localhost:8080/spring-ai/joke'

```

curl -X POST "http://localhost:8080/spring-ai/ingest-document" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
[
    {
        "content":"Best place to live in the UK is Slough",
        "metaData": [
            {
                "key": "meta_key_1",
                "value": "meta_value_1"
            }
        ] 
    },
    {
        "content":"Best car to buy in the UK is Jaecoo",
        "metaData": [
            {
                "key": "meta_key_2",
                "value": "meta_value_2"
            }
        ] 
    }
]
EOF

```

    curl -X DELETE "http://localhost:8080/spring-ai/clear-vector-db"

```
curl -X POST "http://localhost:8080/spring-ai/ingest-json" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
[
    {
        "content":"Vellore is a district capital in Tamil Nadu, India",
        "metaData": [
            {
                "key": "meta_key_1",
                "value": "meta_value_1"
            }
        ] 
    },
    {
        "content":"Yamaha RX100 motorcyle was loves by 90s Kids",
        "metaData": [
            {
                "key": "meta_key_2",
                "value": "meta_value_2"
            }
        ] 
    }
]
EOF
```

    curl -X GET -G 'http://localhost:8080/spring-ai/rag-chat' --data-urlencode "q=What is the best place to live in the UK"

    curl -X GET -G 'http://localhost:8080/spring-ai/rag-chat-tools-callback' --data-urlencode "q=How many tickets are available for Avatar Movie?"
    
    curl -X GET -G 'http://localhost:8080/spring-ai/rag-chat-tools-callback' --data-urlencode "q=Book 3 tickets for Avatar Movie"

    curl -X GET -G 'http://localhost:8080/spring-ai/secure-rag-chat' --header "Authorization: Bearer user-token-1" --data-urlencode "q=What is the best place to live in the UK"

    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/spring-ai/ingest-pdf" -F "file=@$HOME/Downloads/International_Cricket_Council.pdf;type=application/pdf"

    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/spring-ai/image" -F "q=Explain what do you see on this picture?" -F "file=@$HOME/Documents/llama_image.jpeg;type=image/jpeg"

    curl -X GET -G 'http://localhost:8080/spring-ai/validate-vehicle' --data-urlencode "registration=XXX YYY"

    curl -X GET -G 'http://localhost:8080/spring-ai/validate-vehicle' --data-urlencode "registration=ZZZ YYY"

```
curl -X POST "http://localhost:8080/spring-ai/chat-with-memory" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
{
    "chat":"Tell me about Acme Limited" 
}
EOF
```

```
curl -X POST "http://localhost:8080/spring-ai/ingest-secure-document" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
[
  {
    "content": "I like to play and watch Cricket",
    "metaData": {
      "id": "user-name-1",
      "type": "USER"
    }
  },
  {
    "content": "I like to play and watch Football",
    "metaData": {
      "id": "user-name-2",
      "type": "USER"
    }
  },
  {
    "content": "Total sales of Store 01 in 2024 is £5000",
    "metaData": {
      "id": "store-01",
      "type": "STORE"
    }
  },
  {
    "content": "Total sales of Store 02 in 2024 is £3000",
    "metaData": {
      "id": "store-02",
      "type": "STORE"
    }
  }
]
EOF
```

```
curl http://localhost:11434/api/embed -d '{
  "model": "mxbai-embed-large",
  "input": "Llamas are members of the camelid family"
}'
```