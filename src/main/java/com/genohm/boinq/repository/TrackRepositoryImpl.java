package com.genohm.boinq.repository;

import java.util.Optional;

import javax.inject.Inject;

import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.service.FileManagerService;
import com.genohm.boinq.service.MetaInfoService;

public class TrackRepositoryImpl implements TrackRepositoryExtensions {
	
	@Inject
	private TrackRepository trackRepository;
	@Inject
	private FileManagerService fileManager;
	@Inject
	private MetaInfoService metaInfoService;
	
	@Override
	public void deleteFiles(Track track) {
		for (RawDataFile file: track.getRawDataFiles()) {
			fileManager.remove(file);
		}
	}

	@Override
	public Optional<Track> findOneWithMeta(Long id) {
		Optional<Track> result = trackRepository.findOneById(id);
		if (result.isPresent()) {
			metaInfoService.addMetaInfo(result.get());
		}
		return result;
	}	
}
