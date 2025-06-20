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

public class RagControllerIT extends AbstractBaseIT {

	private final RagVectorRepository ragVectorRepository;
	private final JsonUtil jsonUtil;

	@Autowired
	public RagControllerIT(
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

	@Test
	public void get_real_time_info_from_multiple_tools() {
		Response aiResponse = restClient()
			.contentType(ContentType.TEXT)
			.accept(ContentType.TEXT)
			.queryParam("q", """
				How many tickets are available for Avatar Movie?
				 And what is the weather in London?
				""")
			.get("/rag-chat-tools-callback")
			.andReturn();

		assertThat(aiResponse.statusCode()).isEqualTo(200);
		assertThat(aiResponse.getBody().asPrettyString())
			.containsIgnoringCase("tickets available")
			.containsIgnoringCase("Avatar")
			.containsIgnoringCase("weather")
			.containsIgnoringCase("London")
			.satisfiesAnyOf(
				resp -> assertThat(resp).containsIgnoringCase("20 degrees"),
				resp -> assertThat(resp).containsIgnoringCase("20C"),
				resp -> assertThat(resp).containsIgnoringCase("20°C")
			);
	}

	@Test
	public void take_real_time_action_using_tools() {
		Response aiResponse = restClient()
			.contentType(ContentType.TEXT)
			.accept(ContentType.TEXT)
			.queryParam("q", """
				Book 3 tickets for Avatar Movie
				""")
			.get("/rag-chat-tools-callback")
			.andReturn();

		assertThat(aiResponse.statusCode()).isEqualTo(200);
		assertThat(aiResponse.getBody().asPrettyString())
			.satisfiesAnyOf(
				resp -> assertThat(resp).containsIgnoringCase("3 tickets"),
				resp -> assertThat(resp).containsIgnoringCase("3 seats")
			);

		assertThat(aiResponse.getBody().asPrettyString())
			.containsIgnoringCase("success")
			.containsIgnoringCase("Avatar");
	}
}
