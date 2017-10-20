package com.genohm.boinq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.RegionOfInterest;

/**
 * Spring Data JPA repository for the RegionOfInterest entity.
 */
public interface RegionOfInterestRepository extends JpaRepository<RegionOfInterest, Long> {

}
