package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.AuthorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/books")
public class BookController {

	private static final String THINK_END_TAG = "</think>";
	private final Logger log = LoggerFactory.getLogger(BookController.class);
	private final ChatClient chatClient;

	@Autowired
	public BookController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("by-author")
	public AuthorResult byAuthor(
		@RequestParam("author") String author) {
		String promptMessage = """
			Generate a list of books written by the author {author}. If you aren't positive that a book belongs to this author please don't include it.
			{format}
			""";
		BeanOutputConverter<AuthorResult> outputParser = new BeanOutputConverter<>(AuthorResult.class);
		String format = outputParser.getFormat();
		log.info("format {}", format);
		PromptTemplate promptTemplate = new PromptTemplate(promptMessage,
			Map.of("author", author, "format", format));
		Message userMessage = promptTemplate.createMessage();

		Prompt prompt = new Prompt(List.of(userMessage));
		log.info("prompt {}", prompt);
		String content = Objects.requireNonNull(chatClient.prompt(prompt)
			.call()
			.content());
		log.info("raw content {}", content);
		AuthorResult result = outputParser.convert(withoutThink(content));
		log.info("parsed AuthorResult {}", result);
		return result;
	}

	private static String withoutThink(String input) {
		int index = input.indexOf(THINK_END_TAG);
		if (index > -1 && input.length() >= index + 8) {
			return input.substring(index + 8);
		} else {
			return input;
		}
	}
}
