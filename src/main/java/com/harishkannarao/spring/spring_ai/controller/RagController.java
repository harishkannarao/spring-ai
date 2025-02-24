package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.InputDocument;
import com.harishkannarao.spring.spring_ai.entity.InputMetaData;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RagController {

	private final ChatModel chatModel;
	private final VectorStore vectorStore;
	private final TokenTextSplitter tokenTextSplitter;

	@Autowired
	public RagController(ChatModel chatModel,
											 VectorStore vectorStore,
											 TokenTextSplitter tokenTextSplitter) {
		this.chatModel = chatModel;
		this.vectorStore = vectorStore;
		this.tokenTextSplitter = tokenTextSplitter;
	}

	@PostMapping("/ingest-document")
	public ResponseEntity<Void> ingestDocument(
		@RequestBody List<InputDocument> input) {
		List<Document> vectorDocuments = input.stream()
			.map(inputDocument -> {
				Map<String, Object> metaData = inputDocument.metaData().stream()
					.collect(Collectors.toUnmodifiableMap(InputMetaData::key, InputMetaData::value));
				return new Document(inputDocument.content(), metaData);
			})
			.toList();
		List<Document> splitDocuments = tokenTextSplitter.apply(vectorDocuments);
		vectorStore.add(splitDocuments);
		return ResponseEntity.accepted().build();
	}
}
