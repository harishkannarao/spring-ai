### Sample http requests with curl

```
curl -X POST "http://localhost:8080/chat-with-context" \
-H "Content-Type: application/json" \
--data-binary @- << 'EOF'
{
    "context":"My name is Harish",
    "question":"What is my name?" 
}
EOF
```

    curl -X POST 'http://localhost:8080/chat' --data-binary 'Tell me about London, England'

    curl -X GET -G 'http://localhost:8080/books/by-author' --data-urlencode "author=Yuval Noah Harari"

    curl 'http://localhost:8080/stream-joke'

    curl 'http://localhost:8080/joke'

```
curl -X POST "http://localhost:8080/ingest-document" \
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