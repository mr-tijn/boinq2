package com.genohm.boinq.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.query.QueryNode;

public interface QueryNodeRepository extends JpaRepository<QueryNode, Long>{
	Optional<QueryNode> findOneById(Long id);
}
