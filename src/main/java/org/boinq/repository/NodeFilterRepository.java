package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.NodeFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeFilterRepository extends JpaRepository<NodeFilter, Long> {
	Optional<NodeFilter> findOneById(Long id);
}
