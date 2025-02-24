package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenSplitterConfiguration {

	@Bean
	public TokenTextSplitter createTokenTextSplitter() {
		return new TokenTextSplitter();
	}
}
