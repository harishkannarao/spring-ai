package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.transformer.KeywordMetadataEnricher;
import org.springframework.ai.chat.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DocumentTransformerConfiguration {

	@Bean
	public TokenTextSplitter tokenTextSplitter() {
		return new TokenTextSplitter();
	}

	@Bean
	public KeywordMetadataEnricher keywordMetadataEnricher(ChatModel chatModel) {
		return new KeywordMetadataEnricher(chatModel, 5);
	}

	@Bean
	public SummaryMetadataEnricher summaryMetadataEnricher(ChatModel chatModel) {
		return new SummaryMetadataEnricher(
			chatModel,
			List.of(
				SummaryMetadataEnricher.SummaryType.PREVIOUS,
				SummaryMetadataEnricher.SummaryType.CURRENT,
				SummaryMetadataEnricher.SummaryType.NEXT)
		);
	}
}
