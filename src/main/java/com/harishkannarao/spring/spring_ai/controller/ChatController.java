package com.harishkannarao.spring.spring_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {

	private final ChatClient chatClient;

	@Autowired
	public ChatController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping("chat")
	public Flux<String> chat(@RequestBody String input) {
		UserMessage userMessage = new UserMessage(input);
		Prompt prompt = new Prompt(List.of(userMessage));
		return chatClient.prompt(prompt)
			.stream()
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
