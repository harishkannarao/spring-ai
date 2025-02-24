package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.InputDocument;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

		return ResponseEntity.accepted().build();
	}
}
