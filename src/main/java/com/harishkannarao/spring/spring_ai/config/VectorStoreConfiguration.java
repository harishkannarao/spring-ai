package com.harishkannarao.spring.spring_ai.config;

import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VectorStoreConfiguration {

	@Bean
	@Primary
	public VectorStore primaryRagVectorStore(
		JdbcTemplate jdbcTemplate,
		EmbeddingModel embeddingModel,
		@Value("${spring.ai.vectorstore.pgvector.schema-name}") String schemaName,
		@Value("${spring.ai.vectorstore.pgvector.table-name}") String tableName,
		@Value("${spring.ai.vectorstore.pgvector.index-type}") PgVectorStore.PgIndexType pgIndexType,
		@Value("${spring.ai.vectorstore.pgvector.dimensions}") int dimensions,
		@Value("${spring.ai.vectorstore.pgvector.distance-type}") PgVectorStore.PgDistanceType pgDistanceType,
		@Value("${spring.ai.vectorstore.pgvector.max-document-batch-size}") int maxDocumentBatchSize,
		@Value("${spring.ai.vectorstore.pgvector.schema-validation}") boolean validationEnabled
	) {
		return PgVectorStore.builder(jdbcTemplate, embeddingModel)
			.idType(PgVectorStore.PgIdType.UUID)
			.schemaName(schemaName)
			.vectorTableName(tableName)
			.indexType(pgIndexType)
			.dimensions(dimensions)
			.distanceType(pgDistanceType)
			.maxDocumentBatchSize(maxDocumentBatchSize)
			.vectorTableValidationsEnabled(validationEnabled)
			.build();
	}

	@Bean
	@Qualifier("secureRagVectorStore")
	public VectorStore secureRagVectorStore(
		JdbcTemplate jdbcTemplate,
		EmbeddingModel embeddingModel,
		@Value("${spring.ai.vectorstore.pgvector.schema-name}") String schemaName,
		@Value("${app.ai.secure_rag.table_name}") String tableName,
		@Value("${spring.ai.vectorstore.pgvector.index-type}") PgVectorStore.PgIndexType pgIndexType,
		@Value("${spring.ai.vectorstore.pgvector.dimensions}") int dimensions,
		@Value("${spring.ai.vectorstore.pgvector.distance-type}") PgVectorStore.PgDistanceType pgDistanceType,
		@Value("${spring.ai.vectorstore.pgvector.max-document-batch-size}") int maxDocumentBatchSize,
		@Value("${spring.ai.vectorstore.pgvector.schema-validation}") boolean validationEnabled
	) {
		return PgVectorStore.builder(jdbcTemplate, embeddingModel)
			.idType(PgVectorStore.PgIdType.UUID)
			.schemaName(schemaName)
			.vectorTableName(tableName)
			.indexType(pgIndexType)
			.dimensions(dimensions)
			.distanceType(pgDistanceType)
			.maxDocumentBatchSize(maxDocumentBatchSize)
			.vectorTableValidationsEnabled(validationEnabled)
			.build();
	}

	@Bean
	@Qualifier("chatHistoryVectorStore")
	public VectorStore chatHistoryVectorStore(
		JdbcTemplate jdbcTemplate,
		EmbeddingModel embeddingModel,
		@Value("${spring.ai.vectorstore.pgvector.schema-name}") String schemaName,
		@Value("${app.ai.chat_history.table_name}") String tableName,
		@Value("${spring.ai.vectorstore.pgvector.index-type}") PgVectorStore.PgIndexType pgIndexType,
		@Value("${spring.ai.vectorstore.pgvector.dimensions}") int dimensions,
		@Value("${spring.ai.vectorstore.pgvector.distance-type}") PgVectorStore.PgDistanceType pgDistanceType,
		@Value("${spring.ai.vectorstore.pgvector.max-document-batch-size}") int maxDocumentBatchSize,
		@Value("${spring.ai.vectorstore.pgvector.schema-validation}") boolean validationEnabled
	) {
		return PgVectorStore.builder(jdbcTemplate, embeddingModel)
			.idType(PgVectorStore.PgIdType.UUID)
			.schemaName(schemaName)
			.vectorTableName(tableName)
			.indexType(pgIndexType)
			.dimensions(dimensions)
			.distanceType(pgDistanceType)
			.maxDocumentBatchSize(maxDocumentBatchSize)
			.vectorTableValidationsEnabled(validationEnabled)
			.build();
	}
}
