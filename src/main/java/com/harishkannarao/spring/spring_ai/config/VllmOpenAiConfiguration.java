package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class VllmOpenAiConfiguration {

	@Bean
	@ConditionalOnProperty(name = "app.ai.chat.provider", havingValue = "vllm")
	@Primary
	public ChatModel defaultChatModel(Environment env) {
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory =
			new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setReadTimeout(Duration.parse(env.getRequiredProperty("vllm.chat.timeout")));
		RestClient.Builder clientBuilder =
			RestClient.builder().requestFactory(simpleClientHttpRequestFactory);
		OpenAiApi openAiApi = OpenAiApi.builder()
			.apiKey(env.getRequiredProperty("vllm.chat.api-key"))
			.baseUrl(env.getRequiredProperty("vllm.chat.base-url"))
			.completionsPath(env.getRequiredProperty("vllm.chat.completions-path"))
			.restClientBuilder(clientBuilder)
			.build();

		OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
			.model(env.getRequiredProperty("vllm.chat.model"))
			.temperature(0.8)
			.build();

		return OpenAiChatModel.builder()
			.openAiApi(openAiApi)
			.defaultOptions(openAiChatOptions)
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
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory =
			new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setReadTimeout(Duration.parse(env.getRequiredProperty("vllm.embedding.timeout")));
		RestClient.Builder clientBuilder =
			RestClient.builder().requestFactory(simpleClientHttpRequestFactory);

		OpenAiApi openAiApi = OpenAiApi.builder()
			.apiKey(env.getRequiredProperty("vllm.embedding.api-key"))
			.baseUrl(env.getRequiredProperty("vllm.embedding.base-url"))
			.completionsPath(env.getRequiredProperty("vllm.embedding.completions-path"))
			.restClientBuilder(clientBuilder)
			.build();

		return new OpenAiEmbeddingModel(
			openAiApi,
			MetadataMode.EMBED,
			OpenAiEmbeddingOptions.builder()
				.model(env.getRequiredProperty("vllm.embedding.model"))
				.build()
		);
	}
}
