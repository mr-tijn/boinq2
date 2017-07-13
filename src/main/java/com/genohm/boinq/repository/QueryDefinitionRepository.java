package com.genohm.boinq.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.query.QueryDefinition;

public interface QueryDefinitionRepository extends JpaRepository<QueryDefinition, Long>, QueryDefinitionRepositoryExtensions {
	Optional<QueryDefinition> findOneById(Long id);
}
