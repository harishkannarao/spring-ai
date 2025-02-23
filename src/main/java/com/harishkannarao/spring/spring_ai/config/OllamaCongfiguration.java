package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(name = "app.ai.engine", havingValue = "ollama")
public class OllamaCongfiguration {

	@Bean
	@Primary
	public ChatModel defaultChatModel(OllamaChatModel ollamaChatModel) {
		return ollamaChatModel;
	}

	@Bean
	@Primary
	public EmbeddingModel defaultEmbeddingModel(OllamaEmbeddingModel ollamaEmbeddingModel) {
		return ollamaEmbeddingModel;
	}
}
