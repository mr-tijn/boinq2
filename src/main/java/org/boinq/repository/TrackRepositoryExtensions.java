package org.boinq.repository;

import java.util.Optional;

import org.boinq.domain.Track;

public interface TrackRepositoryExtensions {
	void deleteFiles(Track track);
	void empty(Track track);
	Optional<Track> findOneWithMeta(Long id);
}
