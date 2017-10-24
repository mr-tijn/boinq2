package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.QueryDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryDefinitionRepository extends JpaRepository<QueryDefinition, Long>, QueryDefinitionRepositoryExtensions {
	Optional<QueryDefinition> findOneById(Long id);
}
