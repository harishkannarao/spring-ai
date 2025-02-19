package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {

	@Bean
	public ChatClient createChatClient(ChatClient.Builder builder) {
		return builder
			.defaultSystem("You are a helpful AI Assistant answering questions")
			.build();
	}
}
