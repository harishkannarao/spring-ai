package com.harishkannarao.spring.spring_ai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

@Component
public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

	private final Logger log = LoggerFactory.getLogger(TicketInventoryService.class);

	private final Map<String, BigDecimal> weather = Map.ofEntries(
		Map.entry("london", new BigDecimal("20")),
		Map.entry("chennai", new BigDecimal("37"))
	);

	@Override
	public Response apply(Request request) {
		log.info("request {}", request);
		BigDecimal value = weather.getOrDefault(request.location().toLowerCase(), new BigDecimal("25"));
		Response response = new Response(value, request.unit());
		log.info("response {}", response);
		return response;
	}

	public record Request(
		@ToolParam(description = "Town or City or Village or Location of the weather") String location,
		@ToolParam(description = "Unit in Celsius or Fahrenheit") Unit unit) {
	}

	public enum Unit {
		Celsius, Fahrenheit
	}

	public record Response(
		BigDecimal weather,
		Unit unit) {
	}
}
