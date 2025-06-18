package com.harishkannarao.spring.spring_ai.integration;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageControllerIT extends AbstractBaseIT {

	@Test
	public void extract_text_from_image() throws IOException {
		String input = "Explain what do you see on this picture?";
		File inputImage = new ClassPathResource("/images/client_server_history.png").getFile();
		Response response = restClient()
			.accept(ContentType.TEXT)
			.contentType(ContentType.MULTIPART)
			.multiPart("q", input)
			.multiPart(new MultiPartSpecBuilder(inputImage)
				.controlName("file")
				.fileName("client_server_history.png")
				.mimeType(MediaType.IMAGE_PNG_VALUE)
				.build())
			.post("/image")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body().asString())
			.containsIgnoringCase("corba")
			.containsIgnoringCase("soap")
			.containsIgnoringCase("rest")
			.containsIgnoringCase("graphql")
			.containsIgnoringCase("mcp");
	}
}
