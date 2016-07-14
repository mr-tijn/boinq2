package com.genohm.boinq.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.query.GraphTemplate;

public interface GraphTemplateRepository extends JpaRepository<GraphTemplate, Long>{
	Optional<GraphTemplate> findOneById(Long id);

}

