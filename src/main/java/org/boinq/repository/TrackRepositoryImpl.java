package org.boinq.repository;

import java.util.Optional;

import javax.inject.Inject;

import org.boinq.domain.RawDataFile;
import org.boinq.domain.Track;
import org.boinq.service.FileManagerService;
import org.boinq.service.LocalGraphService;
import org.boinq.service.MetaInfoService;

public class TrackRepositoryImpl implements TrackRepositoryExtensions {
	
	@Inject
	private TrackRepository trackRepository;
	@Inject
	private FileManagerService fileManager;
	@Inject
	private MetaInfoService metaInfoService;
	@Inject
	private LocalGraphService localGraphService;
	
	@Override
	public void deleteFiles(Track track) {
		for (RawDataFile file: track.getRawDataFiles()) {
			fileManager.remove(file);
		}
	}
	
	@Override
	public void empty(Track track) {
		localGraphService.deleteGraph(track.getGraphName());
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
