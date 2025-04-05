package com.harishkannarao.spring.spring_ai.configuration;

import com.harishkannarao.spring.spring_ai.container.PgVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@TestConfiguration
public class IntegrationConfiguration {

	private final Logger log = LoggerFactory.getLogger(IntegrationConfiguration.class);
	

	@Bean
	public PgVector startPgVector() {
		log.info("Starting PgVector");
		PgVector pgVector = new PgVector();
		pgVector.getContainer().start();
		try {
			Thread.sleep(Duration.ofSeconds(1));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		await()
			.atMost(Duration.ofMinutes(2))
			.untilAsserted(() -> assertThat(pgVector.getContainer().getLogs()).contains("database system is ready to accept connections"));
		log.info("Started PgVector on port {}", pgVector.getMappedPort());
		return pgVector;
	}

	@Bean
	public DisposableBean stopPgVector(PgVector pgVector) {
		return () -> {
			log.info("Stopping pgVector on port {}", pgVector.getMappedPort());
			pgVector.getContainer().stop();
			log.info("Stopped pgVector");
		};
	}

	@Bean
	public DynamicPropertyRegistrar registerPgVector(PgVector pgVector) {
		return (DynamicPropertyRegistry registry) -> {
			log.info("Registering pgVector properties");
			registry.add("spring.datasource.hikari.jdbc-url", pgVector::getJdbcUrl);
			registry.add("spring.datasource.hikari.username", pgVector::getUsername);
			registry.add("spring.datasource.hikari.password", pgVector::getPassword);
		};
	}
}
