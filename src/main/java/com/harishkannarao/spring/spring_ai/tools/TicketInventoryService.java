package com.harishkannarao.spring.spring_ai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.function.Function;

@Component
public class TicketInventoryService
	implements Function<TicketInventoryService.Request, TicketInventoryService.Response> {

	private static final Logger log = LoggerFactory.getLogger(TicketInventoryService.class);

	@Override
	public Response apply(Request request) {
		log.info("request {}", request);
		Response response = new Response(Math.abs(new SecureRandom().nextLong()));
		log.info("response {}", response);
		return response;
	}

	public record Request(@ToolParam(description = "The name of a movie") String movieName) {
	}

	public record Response(Long availability) {
	}
}


