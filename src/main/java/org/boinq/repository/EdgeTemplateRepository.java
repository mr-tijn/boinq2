package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.EdgeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EdgeTemplateRepository extends JpaRepository<EdgeTemplate, Long> {
	Optional<EdgeTemplate> findOneById(Long id);
}
