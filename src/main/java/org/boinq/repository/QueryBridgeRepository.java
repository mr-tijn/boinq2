package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.query.QueryBridge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryBridgeRepository extends JpaRepository<QueryBridge, Long> {
	Optional<QueryBridge> findOneById(Long id);

}
