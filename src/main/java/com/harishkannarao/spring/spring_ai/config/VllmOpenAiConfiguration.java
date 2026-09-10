package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.time.Duration;

@Configuration
public class VllmOpenAiConfiguration {

	@Bean
	@ConditionalOnProperty(name = "app.ai.chat.provider", havingValue = "vllm")
	@Primary
	public ChatModel defaultChatModel(Environment env) {
		return OpenAiChatModel.builder()
			.options(OpenAiChatOptions.builder()
				.timeout(Duration.parse(env.getRequiredProperty("vllm.chat.timeout")))
				.baseUrl(env.getRequiredProperty("vllm.chat.base-url"))
				.apiKey(env.getRequiredProperty("vllm.chat.api-key"))
				.model(env.getRequiredProperty("vllm.chat.model"))
				.temperature(0.8)
				.build())
			.build();
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.translator.provider", havingValue = "vllm")
	@Qualifier("translatorModel")
	public ChatModel defaultTranslatorModel(Environment env) {
		return defaultChatModel(env);
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.image-extraction.provider", havingValue = "vllm")
	@Qualifier("imageExtractionModel")
	public ChatModel defaultImageExtractionModel(Environment env) {
		return defaultChatModel(env);
	}

	@Bean
	@ConditionalOnProperty(name = "app.ai.embedding.provider", havingValue = "vllm")
	@Primary
	public EmbeddingModel defaultEmbeddingModel(Environment env) {
		return OpenAiEmbeddingModel.builder()
			.metadataMode(MetadataMode.EMBED)
			.options(OpenAiEmbeddingOptions.builder()
				.timeout(Duration.parse(env.getRequiredProperty("vllm.embedding.timeout")))
				.baseUrl(env.getRequiredProperty("vllm.embedding.base-url"))
				.apiKey(env.getRequiredProperty("vllm.embedding.api-key"))
				.model(env.getRequiredProperty("vllm.embedding.model"))
				.build())
			.build();
	}
}
