package com.harishkannarao.spring.spring_ai.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TranslateControllerTest {

	private final ChatClient chatClient = mock();
	private final ChatClient.ChatClientRequestSpec chatClientRequestSpec = mock();
	private final ChatClient.CallResponseSpec callResponseSpec = mock();

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
			.standaloneSetup(new TranslationController(chatClient, 800))
			.build();
	}

	@Test
	public void translate_returns_response_successfully() throws Exception {
		String input = "How are you?";
		String expectedResponse = "Output";
		String sourceLanguage = "English";
		String targetLanguage = "Tamil";

		when(chatClient.prompt(assertArg((Prompt prompt) -> {
			String contents = prompt.getContents();
			assertThat(contents).contains(input);
			assertThat(contents).contains("Translate from " + sourceLanguage);
			assertThat(contents).contains("to " + targetLanguage);
		}))).thenReturn(chatClientRequestSpec);
		when(chatClientRequestSpec.system(assertArg((String systemText) -> {
			assertThat(systemText)
				.contains("You are a professional translator specializing in literal translation");
		}))).thenReturn(chatClientRequestSpec);
		when(chatClientRequestSpec.call()).thenReturn(callResponseSpec);
		when(callResponseSpec.content()).thenReturn(expectedResponse);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.post("/translate")
				.content(input)
				.queryParam("sourceLang", sourceLanguage)
				.queryParam("targetLang", targetLanguage)
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN)
			)
			.andDo(print())
			.andReturn()
			.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

		verify(chatClient, times(1)).prompt(any(Prompt.class));
		verify(chatClientRequestSpec, times(1)).system(any(String.class));
		verify(chatClientRequestSpec, times(1)).call();
		verify(callResponseSpec, times(1)).content();
	}

	@Test
	public void translate_returns_response_successfully_using_smaller_chunks() throws Exception {
		String input = """
			This is a long piece of text, that needs to be broken down into smaller chunks before
			 translating.
			This is expected to work as expected.
			""";
		String expectedChunk1 = "This is a long piece of text, that needs";
		String expectedChunk2 = """
			to be broken down into smaller chunks before
			 translating""";
		String expectedChunk3 = """
			.
			This is expected to work as expected.""";
		String expectedResponse1 = "Output1";
		String expectedResponse2 = "Output2";
		String sourceLanguage = "English";
		String targetLanguage = "Tamil";

		MockMvc mockMvcWithSmallChunks = MockMvcBuilders
			.standaloneSetup(new TranslationController(chatClient, 10))
			.build();

		when(chatClient.prompt(assertArg((Prompt prompt) -> {
			String contents = prompt.getContents();
			assertThat(contents)
				.containsAnyOf(expectedChunk1, expectedChunk2, expectedChunk3);
			assertThat(contents).contains("Translate from " + sourceLanguage);
			assertThat(contents).contains("to " + targetLanguage);
		}))).thenReturn(chatClientRequestSpec);
		when(chatClientRequestSpec.system(assertArg((String systemText) -> {
			assertThat(systemText)
				.contains("You are a professional translator specializing in literal translation");
		}))).thenReturn(chatClientRequestSpec);
		when(chatClientRequestSpec.call()).thenReturn(callResponseSpec);
		when(callResponseSpec.content())
			.thenReturn(expectedResponse1)
			.thenReturn(expectedResponse2)
			.thenReturn("");

		MockHttpServletResponse response = mockMvcWithSmallChunks.perform(MockMvcRequestBuilders
				.post("/translate")
				.content(input)
				.queryParam("sourceLang", sourceLanguage)
				.queryParam("targetLang", targetLanguage)
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.TEXT_PLAIN)
			)
			.andDo(print())
			.andReturn()
			.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsString())
			.contains("""
				%s
				%s
				""".formatted(expectedResponse1, expectedResponse2));

		verify(chatClient, atLeast(2)).prompt(any(Prompt.class));
		verify(chatClientRequestSpec, atLeast(2)).system(any(String.class));
		verify(chatClientRequestSpec,atLeast(2)).call();
		verify(callResponseSpec, atLeast(2)).content();
	}
}
