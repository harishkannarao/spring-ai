CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store (
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_time timestamp DEFAULT (timezone('utc', now())) NOT NULL,
	content text,
	metadata json,
	embedding vector(${embeddingDimensions})
);

CREATE INDEX ON vector_store USING ${vectorIndexType} (embedding vector_cosine_ops);