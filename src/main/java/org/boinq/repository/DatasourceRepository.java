package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.Datasource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Datasource entity.
 */
public interface DatasourceRepository extends JpaRepository<Datasource, Long>, DatasourceRepositoryExtensions {
	Optional<Datasource> findOneById(Long id);
	Optional<Datasource> findOneByTracksId(Long id);
}
