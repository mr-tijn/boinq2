package com.genohm.boinq.repository;

import com.genohm.boinq.domain.Datasource;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Datasource entity.
 */
public interface DatasourceRepository extends JpaRepository<Datasource, Long> {
	Optional<Datasource> findOneById(Long id);
}
