package org.boinq.repository;

import java.util.List;
import java.util.Optional;

import org.boinq.domain.Datasource;

public interface DatasourceRepositoryExtensions {
	Optional<Datasource> findOneWithMeta(Long id);
	List<Datasource> findAllWithMeta();
}
