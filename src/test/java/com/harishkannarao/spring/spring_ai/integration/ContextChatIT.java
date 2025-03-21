package com.harishkannarao.spring.spring_ai.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContextChatIT extends AbstractBaseIT {

	@Test
	void test_chat_with_context() {
		ChatWithContext request = new ChatWithContext(
			"My name is Harish",
			"What is my name?"
		);
		Response response = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.body(toJson(request))
			.post("/chat-with-context")
			.andReturn();

		assertThat(response.getStatusCode()).isEqualTo(200);
		assertThat(response.getBody().asPrettyString()).contains("Harish");
	}

	public record ChatWithContext(
		String context,
		String question
	) {}

}
