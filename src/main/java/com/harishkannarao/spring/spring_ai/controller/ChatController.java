package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.QuestionWithContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
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

@RestController
public class ChatController {

	private final ChatClient chatClient;
	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/question-template.st");
	private final Logger log = LoggerFactory.getLogger(ChatController.class);

	@Autowired
	public ChatController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping("/simple-chat")
	public ResponseEntity<String> simpleChat(
		@RequestParam("input") String input) {
		UserMessage userMessage = new UserMessage(input);
		Prompt prompt = new Prompt(List.of(userMessage));
		String result = chatClient.prompt(prompt)
			.call()
			.content();
		return ResponseEntity.ok(result);
	}

	@PostMapping("/simple-stream-chat")
	public ResponseEntity<Flux<String>> simpleStreamChat(
		@RequestParam("input") String input) {
		UserMessage userMessage = new UserMessage(input);
		Prompt prompt = new Prompt(List.of(userMessage));
		Flux<String> result = chatClient.prompt(prompt)
			.stream()
			.content();
		return ResponseEntity.ok(result);
	}

	@PostMapping("chat-with-context")
	public String chatWithContext(@RequestBody QuestionWithContext input) {
		log.info("Input {}", input);
		PromptTemplate promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("context", input.context());
		promptTemplate.add("question", input.question());
		Prompt prompt = promptTemplate.create();
		return chatClient.prompt(prompt)
			.call()
			.content();
	}

	@GetMapping("joke")
	public String joke() {
		UserMessage userMessage = new UserMessage("Please tell a new dad joke");
		Prompt prompt = new Prompt(List.of(userMessage));
		return chatClient.prompt(prompt)
			.call()
			.content();
	}

	@GetMapping("stream-joke")
	public Flux<String> streamJoke() {
		UserMessage userMessage = new UserMessage("Please tell a new dad joke");
		Prompt prompt = new Prompt(List.of(userMessage));
		return chatClient.prompt(prompt)
			.stream()
			.content();
	}
}
