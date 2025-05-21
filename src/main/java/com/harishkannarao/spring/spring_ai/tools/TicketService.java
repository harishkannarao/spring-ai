package com.harishkannarao.spring.spring_ai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.harishkannarao.spring.spring_ai.util.Constants.X_REQUEST_ID;

@Component
public class TicketService implements AiTool {

	private static final Logger log = LoggerFactory.getLogger(TicketService.class);

	@Tool(name = "ticketBookingService",
		description = """
			Book tickets for a movie.
			Input parameters are movie name and total number of tickets to book
			""")
	public BookingResponse apply(BookingRequest request) {
		log.info("booking request {}", request);
		List<String> seats = IntStream.range(0, request.count())
			.boxed()
			.map(index -> "A" + (index + 1))
			.toList();
		BookingResponse response = new BookingResponse(true, seats);
		log.info("booking response {}", response);
		return response;
	}

	@Tool(
		name = "ticketInventoryService",
		description = """
			Get the available ticket count by movie name
			""")
	public LookupResponse apply(LookupRequest request, ToolContext toolContext) {
		try {
			Optional.ofNullable(toolContext.getContext().get(X_REQUEST_ID))
				.ifPresent(o -> MDC.put(X_REQUEST_ID, String.valueOf(o)));
			log.info("lookup request {}", request);
			long randomPositiveLong = new SecureRandom().longs(0, 200)
				.findAny()
				.orElse(0);
			LookupResponse response = new LookupResponse(randomPositiveLong);
			log.info("lookup response {}", response);
			return response;
		} finally {
			MDC.remove(X_REQUEST_ID);
		}
	}

	public record LookupRequest(@ToolParam(description = "The name of a movie") String movieName) {
	}

	public record LookupResponse(Long availability) {
	}

	public record BookingRequest(
		@ToolParam(description = "The name of a movie") String movieName,
		@ToolParam(description = "Number of tickets to book") Integer count) {
	}

	public record BookingResponse(
		boolean bookingSuccess,
		List<String> bookedSeats) {
	}
}


