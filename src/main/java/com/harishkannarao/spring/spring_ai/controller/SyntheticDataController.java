package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.AuthorResult;
import com.harishkannarao.spring.spring_ai.entity.FineTuningFormat;
import com.harishkannarao.spring.spring_ai.entity.QuestionAnswer;
import com.harishkannarao.spring.spring_ai.entity.QuestionAnswerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/synthetic-data")
public class SyntheticDataController {

	private final ClassPathResource qaGenerationResource = new ClassPathResource(
		"/prompts/qa-generation.st");
	private final ChatClient chatClient;
	private final Logger log = LoggerFactory.getLogger(SyntheticDataController.class);


	public SyntheticDataController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping(path = "/convert-to-qa", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuestionAnswerWrapper> convertToQa(
		@RequestBody String content,
		@RequestParam(name = "numPairs", defaultValue = "25") Integer numPairs) {
		log.info("Generation {} qa pairs from content {}", numPairs, content);
		BeanOutputConverter<QuestionAnswerWrapper> outputParser =
			new BeanOutputConverter<>(QuestionAnswerWrapper.class);
		Prompt prompt = PromptTemplate.builder()
			.resource(qaGenerationResource)
			.variables(Map.ofEntries(
				Map.entry("num_pairs", numPairs),
				Map.entry("text", content),
				Map.entry("format", outputParser.getFormat())
			))
			.build()
			.create();
		String response = chatClient.prompt(prompt)
			.call()
			.content();
		log.info("Response from LLM {}", response);
		QuestionAnswerWrapper result = outputParser.convert(Objects.requireNonNull(response));
		return ResponseEntity.ok(result);
	}

	@PostMapping(path = "/convert-to-ft",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FineTuningFormat>> convertToFt(
		@RequestBody QuestionAnswerWrapper input) {
		log.info("Received qa pairs {}", input);
		List<FineTuningFormat> response = input.qaPairs().stream()
			.map(qa -> new FineTuningFormat(List.of(
				new FineTuningFormat.Content("You are a helpful assistant.", FineTuningFormat.Content.Role.system),
				new FineTuningFormat.Content(qa.question(), FineTuningFormat.Content.Role.user),
				new FineTuningFormat.Content(qa.answer(), FineTuningFormat.Content.Role.assistant)
			)))
			.toList();
		log.info("Response as ft {}", response);
		return ResponseEntity.ok(response);
	}
}
