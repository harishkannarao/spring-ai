package com.harishkannarao.spring.spring_ai.runner;

import com.harishkannarao.spring.spring_ai.SpringAiApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import java.util.Properties;

public class ApplicationLocalOpenAiRunner {
	public static void main(String[] args) {
		final Properties properties = new Properties();
		properties.setProperty("spring.profiles.active", "local,openai");
		String[] appArguments = properties.entrySet()
			.stream()
			.map(entry -> "--%s=%s".formatted(entry.getKey(), entry.getValue()))
			.toArray(String[]::new);
		SpringApplication.run(SpringAiApplication.class, appArguments);

		final Logger logger
			= LoggerFactory.getLogger(ApplicationLocalOpenAiRunner.class);
		logger.info("Application Started and integrated with Open AI");
	}

}
