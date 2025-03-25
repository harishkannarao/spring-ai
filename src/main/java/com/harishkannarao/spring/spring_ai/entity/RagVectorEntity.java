package com.harishkannarao.spring.spring_ai.entity;

import org.postgresql.util.PGBinaryObject;
import org.postgresql.util.PGobject;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("rag_vector_store")
public record RagVectorEntity(
	@Id UUID id,
	String content,
	Instant createdTime,
	PGobject metadata
) {
}
