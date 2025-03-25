package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.ChatWithMemory;
import com.harishkannarao.spring.spring_ai.entity.QuestionWithContext;
import com.harishkannarao.spring.spring_ai.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
public class ChatMemoryController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/question-template.st");
	private final ChatClient chatClientWithMemory;

	public ChatMemoryController(
		@Qualifier("textChatClientWithMemory") ChatClient chatClientWithMemory) {
		this.chatClientWithMemory = chatClientWithMemory;
	}

	@PostMapping("chat-with-memory")
	public ResponseEntity<String> chatWithContext(@RequestBody ChatWithMemory input) {
		log.info("Input {}", input);
		PromptTemplate promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("context", "");
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
			.header("conversationId", conversationId)
			.body(content);
	}
}
