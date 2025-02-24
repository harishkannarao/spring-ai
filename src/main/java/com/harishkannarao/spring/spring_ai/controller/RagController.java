package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.InputDocument;
import com.harishkannarao.spring.spring_ai.entity.InputMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class RagController {

	private static final Logger log = LoggerFactory.getLogger(RagController.class);
	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/rag-question-template.st");
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

	@GetMapping("rag-chat")
	public Flux<String> chatWithRag(@RequestParam String q) {
		log.info("Question {}", q);
		String documents = Objects.requireNonNull(vectorStore
				.similaritySearch(SearchRequest.builder().query(q).build()))
			.stream()
			.map(Document::getText)
			.collect(Collectors.joining(System.lineSeparator()));
		log.info("RAG documents: {}", documents);
		PromptTemplate promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("input", q);
		promptTemplate.add("documents", documents);
		Message userMessage = promptTemplate.createMessage();
		Message systemMessage = new SystemMessage(
			"You are a helpful AI Assistant answering questions");
		Prompt prompt = promptTemplate.create();
		return chatModel
			.stream(new Prompt(List.of(systemMessage, userMessage)))
			.map(response -> response.getResult().getOutput().getText());
	}

	@PostMapping("/ingest-document")
	public ResponseEntity<Void> ingestDocument(
		@RequestBody List<InputDocument> input) {
		List<Document> vectorDocuments = input.stream()
			.peek(inputDocument -> log.info("Received input {}", inputDocument))
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
