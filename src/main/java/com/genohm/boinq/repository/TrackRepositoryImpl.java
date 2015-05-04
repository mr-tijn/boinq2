package com.genohm.boinq.repository;

import javax.inject.Inject;

import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.service.FileManagerService;

public class TrackRepositoryImpl implements TrackRepositoryExtensions {
	
	@Inject
	FileManagerService fileManager;
	
	@Override
	public void deleteFiles(Track track) {
		for (RawDataFile file: track.getRawDataFiles()) {
			fileManager.remove(file);
		}
		
	}

}
