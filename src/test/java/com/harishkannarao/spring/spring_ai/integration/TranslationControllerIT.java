package com.harishkannarao.spring.spring_ai.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class TranslationControllerIT extends AbstractBaseIT {

	@Test
	public void translate_from_english_to_french() {
		String input = "How are you?";

		Response response = restClient()
			.accept(ContentType.TEXT)
			.contentType(ContentType.TEXT)
			.body(input)
			.queryParam("sourceLang", "English")
			.queryParam("targetLang", "French")
			.post("/translate")
			.andReturn();

		assertThat(response.body().asString())
			.containsAnyOf(
				"Comment es-tu ?",
				"Comment êtes-vous ?",
				"Comment vas-tu ?",
				"Comment ça va ?"
			);
	}

	@Test
	public void transliterate_from_tamil_to_english() {
		String input = "அகர முதல எழுத்தெல்லாம் ஆதி";

		Response response = restClient()
			.accept(ContentType.TEXT)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(input)
			.queryParam("sourceLang", "Tamil")
			.queryParam("targetLang", "English")
			.post("/transliterate")
			.andReturn();

		assertThat(response.body().asString())
			.containsAnyOf(
				"Agaram",
				"Akara",
				"Mudhala",
				"mudhala",
				"Ezhuththellaam",
				"ezhuthellam",
				"Aadhi",
				"aadhi"
			);
	}
}
