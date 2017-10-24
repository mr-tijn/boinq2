package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.QueryEdge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryEdgeRepository extends JpaRepository<QueryEdge, Long> {
	Optional<QueryEdge> findOneById(Long id);
}
