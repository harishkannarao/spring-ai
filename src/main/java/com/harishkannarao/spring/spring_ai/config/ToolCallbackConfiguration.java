package com.harishkannarao.spring.spring_ai.config;

import com.harishkannarao.spring.spring_ai.tools.AiTool;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ToolCallbackConfiguration {

	@Bean
	public List<ToolCallback> createToolCallbacks(List<AiTool> aiTools) {
		ToolCallback[] toolCallbacks = ToolCallbacks.from(aiTools.toArray());
		return Stream.of(toolCallbacks).toList();
	}
}
