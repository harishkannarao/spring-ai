package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.entity.QuestionWithContext;
import com.harishkannarao.spring.spring_ai.util.JsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatControllerIT extends AbstractBaseIT {

	private final JsonUtil jsonUtil;

	@Autowired
	public ChatControllerIT(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}

	@Test
	public void simple_chat() {
		String input = "When did World War II end?";

		Response response = restClient()
			.accept(ContentType.TEXT)
			.queryParam("input", input)
			.post("/simple-chat")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body().asString()).contains("1945");
	}

	@Test
	public void simple_stream_chat() {
		String input = "When did World War II end?";

		Response response = restClient()
			.accept(ContentType.TEXT)
			.queryParam("input", input)
			.post("/simple-stream-chat")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body().asString()).contains("1945");
	}

	@Test
	public void chat_with_context() {
		QuestionWithContext input = new QuestionWithContext(
			"My name is Harish",
			"What is my name?"
		);

		Response response = restClient()
			.accept(ContentType.TEXT)
			.contentType(ContentType.JSON)
			.body(jsonUtil.toJson(input))
			.post("/chat-with-context")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body().asString()).contains("Harish");
	}
}
