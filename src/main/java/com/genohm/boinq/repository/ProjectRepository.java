package com.genohm.boinq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.Project;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
