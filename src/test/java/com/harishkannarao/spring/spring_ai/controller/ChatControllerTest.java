package com.harishkannarao.spring.spring_ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.spring.spring_ai.entity.QuestionWithContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ChatControllerTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
		.findAndRegisterModules();

	private final ChatClient chatClient = mock();
	private final ChatClient.ChatClientRequestSpec chatClientRequestSpec = mock();
	private final ChatClient.CallResponseSpec callResponseSpec = mock();

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
			.standaloneSetup(new ChatController(chatClient))
			.build();
	}

	@Test
	public void chatWithContext_returns_response_successfully() throws Exception {
		QuestionWithContext input = new QuestionWithContext(
			"some-context" + UUID.randomUUID(), "some-question" + UUID.randomUUID());
		String inputJson = OBJECT_MAPPER.writeValueAsString(input);
		String expectedResponse = "some-response" + UUID.randomUUID();

		when(chatClient.prompt(
			assertArg((Prompt prompt) -> assertThat(prompt.getContents())
				.contains(input.context())
				.contains(input.question()))
		)).thenReturn(chatClientRequestSpec);
		when(chatClientRequestSpec.call()).thenReturn(callResponseSpec);
		when(callResponseSpec.content()).thenReturn(expectedResponse);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.post("/chat-with-context")
				.content(inputJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.TEXT_PLAIN)
			)
			.andDo(print())
			.andReturn()
			.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
	}
}
