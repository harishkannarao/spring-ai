### Sample http requests with curl

    curl -X POST 'http://localhost:8080/spring-ai/chat' --data-binary 'Tell me about London, England'

    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/spring-ai/simple-chat" -F "input=Tell me about London, England"

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

    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/spring-ai/ingest-pdf" -F "file=@$HOME/Downloads/International_Cricket_Council.pdf;type=application/pdf"

    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/spring-ai/image" -F "q=Explain what do you see on this picture?" -F "file=@$HOME/Downloads/balloon-png-28098.png;type=image/png"

    curl -X GET -G 'http://localhost:8080/spring-ai/validate-vehicle' --data-urlencode "registration=XXX YYY"

    curl -X GET -G 'http://localhost:8080/spring-ai/validate-vehicle' --data-urlencode "registration=ZZZ YYY"

```
curl http://localhost:11434/api/embed -d '{
  "model": "mxbai-embed-large",
  "input": "Llamas are members of the camelid family"
}'
```