package com.harishkannarao.spring.spring_ai.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public final class PgVector {

	private final int port = 5432;
	private final String dbEnvVar = "POSTGRES_DB";
	private final String userEnvVar = "POSTGRES_USER";
	private final String passEnvVar = "POSTGRES_PASSWORD";

	private final GenericContainer<?> container = new GenericContainer<>(
		DockerImageName.parse("pgvector/pgvector:pg16"))
		.withReuse(true)
		.withExposedPorts(port)
		.withEnv(dbEnvVar, "mydatabase")
		.withEnv(userEnvVar, "myuser")
		.withEnv(passEnvVar, "secret");

	public GenericContainer<?> getContainer() {
		return container;
	}

	public Integer getMappedPort() {
		return container.getMappedPort(port);
	}

	public String getJdbcUrl() {
		return String.format("jdbc:postgresql://localhost:%s/%s",
			getMappedPort(), getDatabase());
	}

	public String getDatabase() {
		return container.getEnvMap().get(dbEnvVar);
	}

	public String getUsername() {
		return container.getEnvMap().get(userEnvVar);
	}

	public String getPassword() {
		return container.getEnvMap().get(passEnvVar);
	}
}
