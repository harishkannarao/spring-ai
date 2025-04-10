package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.security.AuthenticationHelper;
import com.harishkannarao.spring.spring_ai.util.Constants;
import com.harishkannarao.spring.spring_ai.util.ExpressionCreator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class SecureRagController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/question-template.st");

	private final ChatClient chatClientWithTools;
	private final VectorStore vectorStore;
	private final AuthenticationHelper authenticationHelper;
	private final ExpressionCreator expressionCreator;

	@Autowired
	public SecureRagController(
		ChatClient chatClientWithTools,
		VectorStore vectorStore,
		AuthenticationHelper authenticationHelper,
		ExpressionCreator expressionCreator) {
		this.chatClientWithTools = chatClientWithTools;
		this.vectorStore = vectorStore;
		this.authenticationHelper = authenticationHelper;
		this.expressionCreator = expressionCreator;
	}

	@GetMapping("/secure-rag-chat")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String chatWithRagWithTools(
		HttpServletRequest httpServletRequest,
		@RequestParam String q) {
		UserDetails userDetails = authenticationHelper.getCurrentUsername(httpServletRequest);
		log.info("User: {} Question {}", userDetails.getUsername(), q);
		Filter.Expression expression = expressionCreator.create(userDetails);
		String documents = Objects.requireNonNull(vectorStore
				.similaritySearch(SearchRequest.builder()
					.query(q)
					.filterExpression(expression)
					.build()))
			.stream()
			.map(Document::getText)
			.collect(Collectors.joining(System.lineSeparator()));
		log.info("RAG documents: {}", documents);
		PromptTemplate promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("question", q);
		promptTemplate.add("context", documents);
		Message userMessage = promptTemplate.createMessage();
		Message systemMessage = new SystemMessage(
			"You are a helpful AI Assistant answering questions");
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		return chatClientWithTools
			.prompt(prompt)
			.toolContext(Map.of(Constants.X_REQUEST_ID, MDC.get(Constants.X_REQUEST_ID)))
			.call()
			.content();
	}


}
