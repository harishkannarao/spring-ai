package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.entity.ChatHistoryVectorEntity;
import com.harishkannarao.spring.spring_ai.entity.ChatWithMemory;
import com.harishkannarao.spring.spring_ai.entity.InputDocument;
import com.harishkannarao.spring.spring_ai.entity.InputMetaData;
import com.harishkannarao.spring.spring_ai.repository.ChatHistoryVectorRepository;
import com.harishkannarao.spring.spring_ai.repository.RagVectorRepository;
import com.harishkannarao.spring.spring_ai.util.JsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.harishkannarao.spring.spring_ai.controller.ChatMemoryController.CONVERSATION_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class ChatMemoryControllerIT extends AbstractBaseIT {

	private final ChatHistoryVectorRepository chatHistoryVectorRepository;
	private final RagVectorRepository ragVectorRepository;
	private final JsonUtil jsonUtil;


	@Autowired
	public ChatMemoryControllerIT(
		ChatHistoryVectorRepository chatHistoryVectorRepository,
		RagVectorRepository ragVectorRepository, 
		JsonUtil jsonUtil) {
		this.chatHistoryVectorRepository = chatHistoryVectorRepository;
		this.ragVectorRepository = ragVectorRepository;
		this.jsonUtil = jsonUtil;
	}

	@BeforeEach
	public void cleanUp() {
		chatHistoryVectorRepository.deleteAll();
		ragVectorRepository.deleteAll();
	}

	@Test
	public void chat_history_without_conversation_id() {
		assertThat(chatHistoryVectorRepository.count()).isZero();

		ChatWithMemory input1 = new ChatWithMemory(null,
			"""
						My name is Harish.
						I love to play Cricket.""");

		Response response1 = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.body(jsonUtil.toJson(input1))
			.post("/chat-with-memory")
			.andReturn();

		assertThat(response1.getStatusCode()).isEqualTo(200);
		assertThat(response1.getBody().asPrettyString()).contains("Harish");

		String conversationId = response1.getHeader(CONVERSATION_ID);

		List<ChatHistoryVectorEntity> entries = chatHistoryVectorRepository.findAll();

		assertThat(entries)
			.hasSize(2)
			.anySatisfy(entry -> {
				assertThat(entry.content())
					.containsIgnoringWhitespaces("Harish");
				assertThat(entry.metadata().getValue()).contains(conversationId);
			});

		ChatWithMemory input2 = new ChatWithMemory(
			UUID.fromString(conversationId),
			"What is my name?");

		Response response2 = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.body(jsonUtil.toJson(input2))
			.post("/chat-with-memory")
			.andReturn();

		assertThat(response2.getStatusCode()).isEqualTo(200);
		assertThat(response2.getBody().asPrettyString()).contains("Harish");

		ChatWithMemory input3 = new ChatWithMemory(
			UUID.fromString(conversationId),
			"What is the best place to live in the UK based on my interest?");

		Response response3 = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.body(jsonUtil.toJson(input3))
			.post("/chat-with-memory")
			.andReturn();

		assertThat(response3.getStatusCode()).isEqualTo(200);
		assertThat(response3.getBody().asPrettyString()).doesNotContainIgnoringCase("Slough");
		assertThat(response3.getBody().asPrettyString()).containsIgnoringCase("Cricket");

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

		ChatWithMemory input4 = new ChatWithMemory(
			UUID.fromString(conversationId),
			"What is the best place to live in the UK based on my interest?");

		Response response4 = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.body(jsonUtil.toJson(input4))
			.post("/chat-with-memory")
			.andReturn();

		assertThat(response4.getStatusCode()).isEqualTo(200);
		assertThat(response4.getBody().asPrettyString()).containsIgnoringCase("Slough");
		assertThat(response4.getBody().asPrettyString()).containsIgnoringCase("Cricket");
	}
}
