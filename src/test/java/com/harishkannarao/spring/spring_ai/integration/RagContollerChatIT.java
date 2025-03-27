package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.entity.InputDocument;
import com.harishkannarao.spring.spring_ai.entity.InputMetaData;
import com.harishkannarao.spring.spring_ai.entity.RagVectorEntity;
import com.harishkannarao.spring.spring_ai.repository.RagVectorRepository;
import com.harishkannarao.spring.spring_ai.util.JsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RagContollerChatIT extends AbstractBaseIT {

	private final RagVectorRepository ragVectorRepository;
	private final JsonUtil jsonUtil;

	@Autowired
	public RagContollerChatIT(
		RagVectorRepository ragVectorRepository, JsonUtil jsonUtil) {
		this.ragVectorRepository = ragVectorRepository;
		this.jsonUtil = jsonUtil;
	}

	@BeforeEach
	@AfterEach
	public void cleanVectorStore() {
		ragVectorRepository.deleteAll();
	}

	@Test
	public void ingest_and_query_by_RAG() {

		assertThat(ragVectorRepository.count()).isEqualTo(0);

		InputDocument inputDocument = new InputDocument(
			"""
				Slough is the best place to live in the UK
				""",
			List.of(
				new InputMetaData("key", "value"),
				new InputMetaData("created_time", Instant.now().toString())
			)
		);

		Response ingestionResponse = restClient()
			.contentType(ContentType.JSON)
			.body(jsonUtil.toJson(List.of(inputDocument)))
			.post("/ingest-document")
			.andReturn();

		assertThat(ingestionResponse.getStatusCode()).isEqualTo(204);

		List<RagVectorEntity> ragEntries = ragVectorRepository.findAll();

		assertThat(ragEntries)
			.hasSize(1)
			.anySatisfy(entity ->
				assertThat(entity.content()).containsIgnoringWhitespaces(inputDocument.content()));

		Response aiResponse = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.queryParam("q", """
				What is the best place to live in the UK?
				""")
			.get("/rag-chat")
			.andReturn();

		assertThat(aiResponse.statusCode()).isEqualTo(200);
		assertThat(aiResponse.getBody().asPrettyString()).contains("Slough");
	}

	@Test
	public void ingest_and_query_without_RAG() {
		Response aiResponse = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.queryParam("q", """
				What is the best place to live in the UK?
				""")
			.get("/rag-chat")
			.andReturn();

		assertThat(aiResponse.statusCode()).isEqualTo(200);
		assertThat(aiResponse.getBody().asPrettyString()).doesNotContain("Slough");
	}
}
