package com.harishkannarao.spring.spring_ai.integration;

import com.harishkannarao.spring.spring_ai.entity.VehicleValidationError;
import com.harishkannarao.spring.spring_ai.entity.VehicleValidationResponse;
import com.harishkannarao.spring.spring_ai.util.JsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class VehicleValidationControllerIT extends AbstractBaseIT {

	private final JsonUtil jsonUtil;

	@Autowired
	public VehicleValidationControllerIT(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}

	@Test
	public void validate_vehicle_without_any_errors() {
		String input = "XXX YYY";

		Response response = restClient()
			.accept(ContentType.JSON)
			.queryParam("registration", input)
			.get("/validate-vehicle")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);
		VehicleValidationResponse result = jsonUtil.fromJson(response.body().asString(), VehicleValidationResponse.class);
		assertThat(result.errors()).isEmpty();
	}

	@Test
	public void validate_vehicle_with_errors() {
		String input = "ZZZ YYY";

		Response response = restClient()
			.accept(ContentType.JSON)
			.queryParam("registration", input)
			.get("/validate-vehicle")
			.andReturn();

		assertThat(response.statusCode()).isEqualTo(200);
		VehicleValidationResponse result = jsonUtil.fromJson(response.body().asString(), VehicleValidationResponse.class);
		VehicleValidationError expectedError1 = new VehicleValidationError("002_1", "Cannot have more than 9 seats");
		VehicleValidationError expectedError2 = new VehicleValidationError("003_1", "Cannot be longer than 600 centimeters");
		assertThat(result.errors())
			.containsExactlyInAnyOrder(expectedError1, expectedError2);
	}
}
