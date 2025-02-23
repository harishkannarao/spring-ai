package com.harishkannarao.spring.spring_ai.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

	@Bean
	@ConfigurationProperties("spring.datasource.hikari")
	public HikariDataSource createDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
}
