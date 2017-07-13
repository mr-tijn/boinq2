package com.genohm.boinq.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.query.QueryGraph;

public interface QueryGraphRepository extends JpaRepository<QueryGraph, Long>, QueryGraphRepositoryExtensions {
	Optional<QueryGraph> findOneById(Long id);
}

