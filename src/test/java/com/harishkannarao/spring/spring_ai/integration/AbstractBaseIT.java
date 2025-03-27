package com.harishkannarao.spring.spring_ai.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.spring.spring_ai.configuration.IntegrationConfiguration;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(value = {
	IntegrationConfiguration.class
})
@ActiveProfiles({"it"})
public abstract class AbstractBaseIT {

	@LocalServerPort
	protected int port;
	@Autowired
	protected ObjectMapper objectMapper;

	protected RequestSpecification restClient() {
		return RestAssured.given()
			.filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
			.baseUri("http://localhost:%s/spring-ai".formatted(port));
	}
}
