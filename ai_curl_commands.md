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

```
curl -X POST "http://localhost:8080/ingest-json" \
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

    curl -X GET -G 'http://localhost:8080/rag-chat' --data-urlencode "q=What is the best place to live in the UK"

    curl --header "Content-Type: multipart/form-data" -X POST "http://localhost:8080/ingest-pdf" -F "file=@$HOME/Downloads/International_Cricket_Council.pdf;type=application/pdf"

```
curl http://localhost:11434/api/embed -d '{
  "model": "mxbai-embed-large",
  "input": "Llamas are members of the camelid family"
}'
```