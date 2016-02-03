package com.genohm.boinq.repository;

import java.util.Optional;

import com.genohm.boinq.domain.Track;

public interface TrackRepositoryExtensions {
	void deleteFiles(Track track);
	Optional<Track> findOneWithMeta(Long id);
}
