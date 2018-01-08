package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.NodeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeTemplateRepository extends JpaRepository<NodeTemplate, Long>{
	Optional<NodeTemplate> findOneById(Long id);
}
