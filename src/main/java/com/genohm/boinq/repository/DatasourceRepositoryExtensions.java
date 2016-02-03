package com.genohm.boinq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.Datasource;

public interface DatasourceRepositoryExtensions {
	Optional<Datasource> findOneWithMeta(Long id);
	List<Datasource> findAllWithMeta();
}
