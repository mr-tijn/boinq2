package com.genohm.boinq.repository;

import com.genohm.boinq.domain.Track;

public interface TrackRepositoryExtensions {
	void deleteFiles(Track track);
}
