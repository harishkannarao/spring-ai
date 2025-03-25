package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.entity.ChatHistoryVectorEntity;
import com.harishkannarao.spring.spring_ai.entity.ChatWithMemory;
import com.harishkannarao.spring.spring_ai.repository.ChatHistoryVectorRepository;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static com.harishkannarao.spring.spring_ai.controller.ChatMemoryController.CONVERSATION_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class ChatMemoryControllerIT extends AbstractBaseIT {

	private final ChatHistoryVectorRepository chatHistoryVectorRepository;

	@Autowired
	public ChatMemoryControllerIT(
		ChatHistoryVectorRepository chatHistoryVectorRepository) {
		this.chatHistoryVectorRepository = chatHistoryVectorRepository;
	}

	@BeforeEach
	public void cleanUp() {
		chatHistoryVectorRepository.deleteAll();
	}

	@Test
	public void chat_history_without_conversation_id() {
		assertThat(chatHistoryVectorRepository.count()).isZero();

		ChatWithMemory input1 = new ChatWithMemory(null, "My name is Harish");

		Response response1 = restClient()
			.contentType(ContentType.JSON)
			.accept(ContentType.TEXT)
			.body(toJson(input1))
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
			.body(toJson(input2))
			.post("/chat-with-memory")
			.andReturn();

		assertThat(response2.getStatusCode()).isEqualTo(200);
		assertThat(response2.getBody().asPrettyString()).contains("Harish");
	}
}
