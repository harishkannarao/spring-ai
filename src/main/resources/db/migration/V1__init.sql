CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS rag_vector_store (
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_time timestamptz DEFAULT (timezone('utc', now())) NOT NULL,
	content text,
	metadata jsonb,
	embedding vector(${embeddingDimensions})
);

CREATE INDEX rag_vector_store_embedding_index ON rag_vector_store USING ${vectorIndexType} (embedding vector_cosine_ops);

CREATE INDEX rag_vector_store_gin_index ON rag_vector_store USING GIN (metadata);