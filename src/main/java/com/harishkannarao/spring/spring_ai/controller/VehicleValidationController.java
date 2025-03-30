package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.VehicleDetails;
import com.harishkannarao.spring.spring_ai.entity.VehicleValidationResponse;
import com.harishkannarao.spring.spring_ai.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class VehicleValidationController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ClassPathResource vehicleValidationTemplate =
		new ClassPathResource("/prompts/vehicle-evaluation-rules-template.st");

	private final ChatClient chatClient;
	private final JsonUtil jsonUtil;

	private final Map<String, VehicleDetails> vehicleDetailsMap = Map.ofEntries(
		Map.entry("XXX YYY",
			new VehicleDetails(
				"XXX YYY",
				LocalDate.now(ZoneOffset.UTC),
				8,
				new VehicleDetails.VehicleDimension(
					400,
					400,
					300))),
		Map.entry("ZZZ YYY",
			new VehicleDetails(
				"ZZZ YYY",
				LocalDate.now(ZoneOffset.UTC).minusYears(20),
				10,
				new VehicleDetails.VehicleDimension(
					750,
					400,
					300)))
	);

	@Autowired
	public VehicleValidationController(
		ChatClient chatClient,
		JsonUtil jsonUtil) {
		this.chatClient = chatClient;
		this.jsonUtil = jsonUtil;
	}

	@GetMapping("/validate-vehicle")
	public VehicleValidationResponse validateVehicle(
		@RequestParam("registration") String registration) {
		log.info("registration {}", registration);
		VehicleDetails vehicleDetails = vehicleDetailsMap.get(registration);
		String vehicleJson = jsonUtil.toJson(vehicleDetails);
		log.info("Vehicle Json: {}", vehicleJson);
		BeanOutputConverter<VehicleValidationResponse> outputParser =
			new BeanOutputConverter<>(VehicleValidationResponse.class);
		String format = outputParser.getFormat();
		log.info("output format {}", format);
		PromptTemplate promptTemplate = new PromptTemplate(
			vehicleValidationTemplate,
			Map.of(
				"registrationCutOff", LocalDate.now(ZoneOffset.UTC).minusYears(20),
				"vehicleDetails", vehicleJson,
				"format", format
			));
		Message userMessage = promptTemplate.createMessage();
		Prompt prompt = new Prompt(List.of(userMessage));
		log.info("prompt {}", prompt);
		String content = Objects.requireNonNull(chatClient.prompt(prompt)
			.call()
			.content());
		log.info("raw response {}", content);
		VehicleValidationResponse result = outputParser.convert(content);
		log.info("parsed response {}", result);
		return result;
	}
}
