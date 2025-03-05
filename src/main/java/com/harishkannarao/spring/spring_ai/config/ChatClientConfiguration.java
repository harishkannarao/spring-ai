package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfiguration {

	@Bean
	public ChatClient createChatClient(
		ChatClient.Builder builder,
		List<ToolCallback> tools) {
		return builder
			.defaultTools(tools)
			.defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}
}
