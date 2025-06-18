package com.harishkannarao.spring.spring_ai.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatControllerIT extends AbstractBaseIT {

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
}
