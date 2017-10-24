package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.QueryGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryGraphRepository extends JpaRepository<QueryGraph, Long>, QueryGraphRepositoryExtensions {
	Optional<QueryGraph> findOneById(Long id);
}

