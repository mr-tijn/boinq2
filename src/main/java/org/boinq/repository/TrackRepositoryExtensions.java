package org.boinq.repository;

import org.boinq.domain.Track;

public interface TrackRepositoryExtensions {
	void deleteFiles(Track track);
	void empty(Track track);
}
