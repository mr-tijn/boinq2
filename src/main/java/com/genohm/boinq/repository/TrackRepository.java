package com.genohm.boinq.repository;

import com.genohm.boinq.domain.Track;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Track entity.
 */
public interface TrackRepository extends JpaRepository<Track, Long>, TrackRepositoryExtensions {
 
}
