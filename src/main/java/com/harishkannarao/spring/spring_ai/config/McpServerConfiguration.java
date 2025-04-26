package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpServerConfiguration {

	@Bean
	public ToolCallbackProvider exposeTools(List<ToolCallback> tools) {
		return ToolCallbackProvider.from(tools);
	}
}
