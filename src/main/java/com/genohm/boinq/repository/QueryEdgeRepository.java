package com.genohm.boinq.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.query.QueryEdge;

public interface QueryEdgeRepository extends JpaRepository<QueryEdge, Long> {
	Optional<QueryEdge> findOneById(Long id);
}
