package com.harishkannarao.spring.spring_ai.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ContextChatIT extends AbstractBaseIT {

	private final TestRestTemplate testRestTemplate;

	@Autowired
	ContextChatIT(TestRestTemplate testRestTemplate) {
		this.testRestTemplate = testRestTemplate;
	}

	@Test
	void test_chat_with_context() {
		ChatWithContext request = new ChatWithContext(
			"My name is Harish",
			"What is my name?"
		);
		ResponseEntity<String> response = testRestTemplate
			.postForEntity("/chat-with-context", request, String.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).contains("Harish");
	}

	public record ChatWithContext(
		String context,
		String question
	) {}

}
