package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.util.JsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ContextChatIT extends AbstractBaseIT {

	private final JsonUtil jsonUtil;

	@Autowired
	ContextChatIT(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}


	@Test
	void test_chat_with_context() {
		ChatWithContext request = new ChatWithContext(
			"My name is Harish",
			"What is my name?"
		);
		Response response = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.body(jsonUtil.toJson(request))
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
