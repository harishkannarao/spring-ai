package com.harishkannarao.spring.spring_ai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class TranslationController {
	private final ChatClient chatClient;
	private final ClassPathResource systemTemplateResource = new ClassPathResource(
		"/prompts/translate-system-template.st");
	private final ClassPathResource userTemplateResource = new ClassPathResource(
		"/prompts/translate-user-template.st");
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public TranslationController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping("/translate")
	public ResponseEntity<String> translate(
		@RequestBody String request,
		@RequestParam String sourceLang,
		@RequestParam String targetLang) {
		TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
		Document inputDocument = new Document(request);
		List<Document> splitDocuments = tokenTextSplitter.apply(List.of(inputDocument));
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemTemplateResource);
		String systemMessage = systemPromptTemplate.createMessage().getText();
		PromptTemplate promptTemplate = new PromptTemplate(userTemplateResource);
		List<String> transformed = splitDocuments.stream()
			.map(document -> {
				Message userMessage = promptTemplate.createMessage(
					Map.of("text", Objects.requireNonNull(document.getText()), "source_lang", sourceLang, "target_lang", targetLang));
				Prompt prompt = new Prompt(userMessage);
				String translatedText = chatClient
					.prompt(prompt)
					.system(systemMessage)
					.call()
					.content();
				log.info("Input {} Output {}", document.getText(), translatedText);
				return translatedText;
			})
			.toList();
		String result = transformed.stream().collect(Collectors.joining(System.lineSeparator()));
		return ResponseEntity.ok(result);
	}
}
