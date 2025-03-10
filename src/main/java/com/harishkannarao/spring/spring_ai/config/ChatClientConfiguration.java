package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class ChatClientConfiguration {

	@Bean
	@Qualifier("textChatClient")
	@Primary
	public ChatClient defaultChatClient(ChatModel chatModel) {
		return ChatClient.builder(chatModel)
			.defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}

	@Bean
	@Qualifier("imageExtractionChatClient")
	public ChatClient defaultImageExtractionChatClient(
		@Qualifier("imageExtractionModel") ChatModel chatModel) {
		return ChatClient.builder(chatModel)
			.defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions about images")
			.build();
	}

	@Bean
	@Qualifier("textChatClientWithTools")
	public ChatClient chatClientWithTools(
		ChatModel chatModel,
		List<ToolCallback> tools) {
		return ChatClient.builder(chatModel)
			.defaultTools(tools)
			.defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}
}
