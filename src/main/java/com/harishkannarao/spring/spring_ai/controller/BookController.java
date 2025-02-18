package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.AuthorResult;
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
    private final ChatClient chatClient;

    @Autowired
    public BookController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful AI Assistant answering questions")
                .build();
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

        PromptTemplate promptTemplate = new PromptTemplate(promptMessage,
                Map.of("author", author, "format", format));
        Message userMessage = promptTemplate.createMessage();

        Prompt prompt = new Prompt(List.of(userMessage));
        String content = Objects.requireNonNull(chatClient.prompt(prompt)
                .call()
                .content());
        return outputParser.convert(withoutThink(content));
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
