package com.harishkannarao.spring.spring_ai.controller;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassPathResourceTest {

	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/question-template.st");

	@Test
	public void testResource() throws IOException {
		String result = questionTemplateResource.getContentAsString(StandardCharsets.UTF_8);
		System.out.println("result = " + result);
		assertThat(result)
			.contains("Question")
			.contains("{context}");
	}
}
