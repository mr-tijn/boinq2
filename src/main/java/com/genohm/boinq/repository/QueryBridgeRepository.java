package com.genohm.boinq.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.query.QueryBridge;

public interface QueryBridgeRepository extends JpaRepository<QueryBridge, Long> {
	Optional<QueryBridge> findOneById(Long id);

}
