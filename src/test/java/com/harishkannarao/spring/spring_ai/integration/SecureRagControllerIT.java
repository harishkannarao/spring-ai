package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.repository.SecureRagVectorRepository;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class SecureRagControllerIT extends AbstractBaseIT {

	private final SecureRagVectorRepository secureRagVectorRepository;
	private final ClassPathResource secureRagDocument = new ClassPathResource(
		"/secure_rag/secure_rag_document.json");

	@Autowired
	public SecureRagControllerIT(SecureRagVectorRepository secureRagVectorRepository) {
		this.secureRagVectorRepository = secureRagVectorRepository;
	}

	@BeforeEach
	public void setUp() {
		secureRagVectorRepository.deleteAll();
	}

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

	@Test
	public void verify_user1_response_with_rag() throws IOException {
		String token = "user-token-1";
		Response aiResponseBeforeRag = restClient()
			.accept(ContentType.TEXT)
			.headers(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.queryParam("q", """
				What is the best place to live in the UK
				""")
			.get("/secure-rag-chat")
			.andReturn();

		assertThat(aiResponseBeforeRag.statusCode()).isEqualTo(200);
		assertThat(aiResponseBeforeRag.body().asString())
			.doesNotContainIgnoringCase("Cricket");

		Response docIngestionResponse = restClient()
			.contentType(ContentType.JSON)
			.body(secureRagDocument.getContentAsString(StandardCharsets.UTF_8))
			.post("/ingest-secure-document")
			.andReturn();

		assertThat(docIngestionResponse.getStatusCode()).isEqualTo(204);

		Response aiResponseAfterRag = restClient()
			.accept(ContentType.TEXT)
			.headers(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.queryParam("q", """
				What is the best place to live in the UK
				""")
			.get("/secure-rag-chat")
			.andReturn();

		assertThat(aiResponseAfterRag.statusCode()).isEqualTo(200);
		assertThat(aiResponseAfterRag.body().asString())
			.containsIgnoringCase("Cricket");
	}
}
