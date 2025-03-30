package com.harishkannarao.spring.spring_ai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.harishkannarao.spring.spring_ai.util.Constants.X_REQUEST_ID;

@Component
public class TicketInventoryService
	implements BiFunction<TicketInventoryService.Request, ToolContext, TicketInventoryService.Response> {

	private static final Logger log = LoggerFactory.getLogger(TicketInventoryService.class);

	@Override
	public Response apply(Request request, ToolContext toolContext) {
		try {
			Optional.ofNullable(toolContext.getContext().get(X_REQUEST_ID))
				.ifPresent(o -> MDC.put(X_REQUEST_ID, String.valueOf(o)));
			log.info("request {}", request);
			long randomPositiveLong = new SecureRandom().longs(0, 200)
				.findAny()
				.orElse(0);
			Response response = new Response(randomPositiveLong);
			log.info("response {}", response);
			return response;
		} finally {
			MDC.remove(X_REQUEST_ID);
		}
	}

	public record Request(@ToolParam(description = "The name of a movie") String movieName) {
	}

	public record Response(Long availability) {
	}
}


