package org.boinq.repository;

import org.boinq.domain.RawDataFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the RawDataFile entity.
 */
public interface RawDataFileRepository extends JpaRepository<RawDataFile, Long> {

}
