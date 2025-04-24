package com.harishkannarao.spring.spring_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.requireNonNull;

@RestController
public class ImageController {
	private final ChatClient imageChatClient;

	@Autowired
	public ImageController(
		@Qualifier("imageExtractionChatClient") ChatClient imageChatClient) {
		this.imageChatClient = imageChatClient;
	}

	@PostMapping(path = "image")
	public ResponseEntity<String> imageChat(
		@RequestParam("q") String input,
		@RequestParam("file") MultipartFile file) {
		UserMessage userMessage = new UserMessage(input,
			new Media(MimeType.valueOf(requireNonNull(file.getContentType())), file.getResource()));
		String result = imageChatClient.prompt(new Prompt(userMessage))
			.call()
			.content();
		return ResponseEntity.ok(result);
	}
}
