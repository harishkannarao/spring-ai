package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.entity.AuthorResult;
import com.harishkannarao.spring.spring_ai.util.JsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class BookControllerIT extends AbstractBaseIT {

	private final JsonUtil jsonUtil;

	@Autowired
	public BookControllerIT(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}

	@Test
	public void books_by_author() {
		String input = "Yuval Noah Harari";

		Response response = restClient()
			.accept(ContentType.JSON)
			.queryParam("author", input)
			.get("/books/by-author")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);
		AuthorResult authorResult = jsonUtil.fromJson(response.body().asString(), AuthorResult.class);
		assertThat(authorResult.author()).isEqualTo(input);
		assertThat(authorResult.books())
			.anySatisfy(bookName -> assertThat(bookName).containsIgnoringCase("A Brief History of Humankind"));
	}
}
