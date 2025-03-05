package com.harishkannarao.spring.spring_ai.config;

import com.harishkannarao.spring.spring_ai.tools.TicketInventoryService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolCallbackConfiguration {

	@Bean
	public ToolCallback createTicketInventoryService(TicketInventoryService ticketInventoryService) {
		return FunctionToolCallback
			.builder("ticketInventoryService", ticketInventoryService)
			.description("Get the available ticket count by movie name")
			.inputType(TicketInventoryService.Request.class)
			.build();
	}
}
