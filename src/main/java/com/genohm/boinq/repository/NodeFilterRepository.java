package com.genohm.boinq.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.query.NodeFilter;

public interface NodeFilterRepository extends JpaRepository<NodeFilter, Long> {
	Optional<NodeFilter> findOneById(Long id);
}
