package com.harishkannarao.spring.spring_ai.repository;

import com.harishkannarao.spring.spring_ai.entity.RagVectorEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface RagVectorRepository extends ListCrudRepository<RagVectorEntity, UUID>  {
}
