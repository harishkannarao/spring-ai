package com.harishkannarao.spring.spring_ai.entity;

import java.util.List;

public record VehicleValidationResponse(
	List<VehicleValidationError> errors
) {
}
