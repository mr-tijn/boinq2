package com.genohm.boinq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.RawDataFile;

/**
 * Spring Data JPA repository for the RawDataFile entity.
 */
public interface RawDataFileRepository extends JpaRepository<RawDataFile, Long> {

}
