package com.harishkannarao.spring.spring_ai.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

public class SecureRagControllerIT extends AbstractBaseIT {

	@Test
	public void returns_401_without_authentication() {
		Response aiResponse = restClient()
			.accept(ContentType.TEXT)
			.queryParam("q", """
				What is the best place to live in the UK
				""")
			.get("/secure-rag-chat")
			.andReturn();

		assertThat(aiResponse.statusCode()).isEqualTo(401);
	}

	@Test
	public void returns_403_for_authenticated_user_without_permission() {
		String token = "basic-user-token";
		Response aiResponse = restClient()
			.accept(ContentType.TEXT)
			.headers(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.queryParam("q", """
				What is the best place to live in the UK
				""")
			.get("/secure-rag-chat")
			.andReturn();

		assertThat(aiResponse.statusCode()).isEqualTo(403);
	}
}
