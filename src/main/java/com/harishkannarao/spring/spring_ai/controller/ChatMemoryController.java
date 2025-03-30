package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.ChatWithMemory;
import com.harishkannarao.spring.spring_ai.entity.DeleteConversationRequest;
import com.harishkannarao.spring.spring_ai.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ChatMemoryController {
	public static final String CONVERSATION_ID = "conversationId";
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/question-template.st");
	private final ChatClient chatClientWithMemory;
	private final VectorStore ragVectorStore;
	private final VectorStore chatVectorStore;

	public ChatMemoryController(
		@Qualifier("textChatClientWithMemory") ChatClient chatClientWithMemory,
		VectorStore ragVectorStore,
		@Qualifier("chatHistoryVectorStore") VectorStore chatVectorStore) {
		this.chatClientWithMemory = chatClientWithMemory;
		this.ragVectorStore = ragVectorStore;
		this.chatVectorStore = chatVectorStore;
	}

	@PostMapping("chat-with-memory")
	public ResponseEntity<String> chatWithContext(@RequestBody ChatWithMemory input) {
		log.info("Input {}", input);
		String documents = Objects.requireNonNull(ragVectorStore
				.similaritySearch(SearchRequest.builder().query(input.chat()).build()))
			.stream()
			.map(Document::getText)
			.collect(Collectors.joining(System.lineSeparator()));
		log.info("RAG documents: {}", documents);
		PromptTemplate promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("context", documents);
		promptTemplate.add("question", input.chat());
		Prompt prompt = promptTemplate.create();
		String conversationId = Objects.requireNonNullElseGet(input.conversationId(), UUID::randomUUID)
			.toString();
		String content = chatClientWithMemory.prompt(prompt)
			.advisors(advisorSpec -> advisorSpec.params(Map.ofEntries(
				Map.entry(Constants.CONVERSATION_ID, conversationId)
			)))
			.call()
			.content();
		return ResponseEntity.ok()
			.header(CONVERSATION_ID, conversationId)
			.body(content);
	}

	@DeleteMapping("/delete-conversation-id")
	public ResponseEntity<Void> deleteConversationId(
		@RequestBody DeleteConversationRequest request
		) {
		FilterExpressionBuilder b = new FilterExpressionBuilder();
		Filter.Expression expression = b.eq("conversationId", request.conversationId().toString()).build();
		chatVectorStore.delete(expression);
		return ResponseEntity.noContent().build();
	}
}
