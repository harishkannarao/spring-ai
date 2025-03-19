package com.harishkannarao.spring.spring_ai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

@Component
public class TicketBookingService
	implements Function<TicketBookingService.Request, TicketBookingService.Response> {

	private static final Logger log = LoggerFactory.getLogger(TicketBookingService.class);

	@Override
	public Response apply(Request request) {
		log.info("request {}", request);
		List<String> seats = IntStream.range(0, request.count())
			.boxed()
			.map(index -> "A" + (index + 1))
			.toList();
		Response response = new Response(true, seats);
		log.info("response {}", response);
		return response;
	}

	public record Request(
		@ToolParam(description = "The name of a movie") String movieName,
		@ToolParam(description = "Number of tickets to book") Integer count) {
	}

	public record Response(
		boolean bookingSuccess,
		List<String> bookedSeats) {
	}
}


