package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class ChatClientConfiguration {

	@Bean
	@Primary
	public ChatClient defaultChatClient(ChatClient.Builder builder) {
		return builder
			.defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}

	@Bean
	@Qualifier("chatClientWithTools")
	public ChatClient chatClientWithTools(
		ChatClient.Builder builder,
		List<ToolCallback> tools) {
		return builder
			.defaultTools(tools)
			.defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}
}
