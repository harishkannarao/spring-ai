package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenAiConfiguration {

	@Bean
	@ConditionalOnProperty(name = "app.ai.chat.provider", havingValue = "openai")
	@Primary
	public ChatModel defaultChatModel(OpenAiChatModel openAiChatModel) {
		return openAiChatModel;
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.translator.provider", havingValue = "openai")
	@Qualifier("translatorModel")
	public ChatModel defaultTranslatorModel(OpenAiChatModel openAiChatModel) {
		return openAiChatModel;
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.image-extraction.provider", havingValue = "openai")
	@Qualifier("imageExtractionModel")
	public ChatModel defaultImageExtractionModel(OpenAiChatModel openAiChatModel) {
		return openAiChatModel;
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.embedding.provider", havingValue = "openai")
	@Primary
	public EmbeddingModel defaultEmbeddingModel(OpenAiEmbeddingModel openAiEmbeddingModel) {
		return openAiEmbeddingModel;
	}
}
