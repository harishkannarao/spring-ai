package com.harishkannarao.spring.spring_ai.entity;

import java.time.LocalDate;

public record VehicleDetails(
	String registration,
	LocalDate dateOfRegistration,
	Integer totalNumberOfSeats,
	VehicleDimension dimension
) {
	public record VehicleDimension(
		Integer lengthInCentimeters,
		Integer widthInCentimeters,
		Integer heightInCentimeters
	) {
	}
}
