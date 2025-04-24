package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
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

	@Bean
	@Qualifier("textChatClientWithMemory")
	public ChatClient chatClientWithMemory(
		ChatModel chatModel,
		@Qualifier("chatHistoryVectorStore") VectorStore chatHistoryVectorStore) {
		VectorStoreChatMemoryAdvisor vectorStoreChatMemoryAdvisor = VectorStoreChatMemoryAdvisor
			.builder(chatHistoryVectorStore)
			.build();
		SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(
			List.of("ACME", "acme", "Acme"),
			"Sorry, I can't respond to this request",
			0);
		return ChatClient.builder(chatModel)
			.defaultAdvisors(List.of(safeGuardAdvisor, vectorStoreChatMemoryAdvisor, new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}
}
