package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.GraphTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraphTemplateRepository extends JpaRepository<GraphTemplate, Long>, GraphTemplateRepositoryExtensions {
	Optional<GraphTemplate> findOneById(Long id);
}

