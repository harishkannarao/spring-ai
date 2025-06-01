package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.bedrock.cohere.BedrockCohereEmbeddingModel;
import org.springframework.ai.bedrock.converse.BedrockProxyChatModel;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AwsBedrockConfiguration {

	@Bean
	@ConditionalOnProperty(name = "app.ai.chat.provider", havingValue = "bedrock-converse")
	@Primary
	public ChatModel defaultChatModel(BedrockProxyChatModel bedrockProxyChatModel) {
		return bedrockProxyChatModel;
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.translator.provider", havingValue = "bedrock-converse")
	@Qualifier("translatorModel")
	public ChatModel defaultTranslatorModel(BedrockProxyChatModel bedrockProxyChatModel) {
		return bedrockProxyChatModel;
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.image-extraction.provider", havingValue = "bedrock-converse")
	@Qualifier("imageExtractionModel")
	public ChatModel defaultImageExtractionModel(BedrockProxyChatModel bedrockProxyChatModel) {
		return bedrockProxyChatModel;
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.embedding.provider", havingValue = "bedrock-converse")
	@Primary
	public EmbeddingModel defaultEmbeddingModel(
		BedrockCohereEmbeddingModel bedrockCohereEmbeddingModel) {
		return bedrockCohereEmbeddingModel;
	}
}
