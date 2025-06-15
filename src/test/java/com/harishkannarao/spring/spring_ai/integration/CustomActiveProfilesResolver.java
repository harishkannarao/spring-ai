package com.harishkannarao.spring.spring_ai.integration;

import org.jetbrains.annotations.NotNull;
import org.springframework.test.context.ActiveProfilesResolver;

import java.util.Optional;

public class CustomActiveProfilesResolver implements ActiveProfilesResolver {
	@NotNull
	@Override
	public String[] resolve(@NotNull Class<?> testClass) {
		String integrationTestActiveProfiles = System.getenv("INTEGRATION_TEST_ACTIVE_PROFILES");
		return Optional.ofNullable(integrationTestActiveProfiles)
			.map(s -> s.split(","))
			.orElse(new String[]{"it"});
	}
}
