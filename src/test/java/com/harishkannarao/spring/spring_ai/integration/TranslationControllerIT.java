package com.harishkannarao.spring.spring_ai.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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

		Assertions.assertThat(response.body().asString())
			.containsAnyOf(
				"Comment es-tu ?",
				"Comment êtes-vous ?",
				"Comment vas-tu ?",
				"Comment ça va ?"
			);
	}
}
