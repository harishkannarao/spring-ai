package com.harishkannarao.spring.spring_ai.repository;

import com.harishkannarao.spring.spring_ai.entity.SecureRagVectorEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface SecureRagVectorRepository
	extends ListCrudRepository<SecureRagVectorEntity, UUID> {
}
