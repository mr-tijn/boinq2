package org.boinq.repository;

import java.util.List;
import java.util.Optional;

import org.boinq.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Track entity.
 */
public interface TrackRepository extends JpaRepository<Track, Long>, TrackRepositoryExtensions {
 
	Optional<Track> findOneById(Long id);
	Optional<List<Track>> findByDatasourceId(Long ds_id);
	
}
