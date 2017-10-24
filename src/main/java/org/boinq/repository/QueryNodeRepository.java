package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.QueryNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryNodeRepository extends JpaRepository<QueryNode, Long>{
	Optional<QueryNode> findOneById(Long id);
}
