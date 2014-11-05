package com.genohm.boinq.repository;

import com.genohm.boinq.domain.RawDataFile;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the RawDataFile entity.
 */
public interface RawDataFileRepository extends JpaRepository<RawDataFile, Long> {

}
