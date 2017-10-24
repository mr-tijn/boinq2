package org.boinq.repository;

import org.boinq.domain.RegionOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the RegionOfInterest entity.
 */
public interface RegionOfInterestRepository extends JpaRepository<RegionOfInterest, Long> {

}
