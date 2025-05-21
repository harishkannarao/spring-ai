package com.harishkannarao.spring.spring_ai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class WeatherService implements AiTool {

	private final Logger log = LoggerFactory.getLogger(WeatherService.class);

	private final Map<String, BigDecimal> weather = Map.ofEntries(
		Map.entry("london", new BigDecimal("20")),
		Map.entry("chennai", new BigDecimal("37"))
	);

	@Tool(
		name = "weatherService",
		description = """
			Get the weather for the given location or village or town or city.
			The request takes location along with unit in Celsius or Fahrenheit.
			""")
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
