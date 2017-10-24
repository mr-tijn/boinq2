package org.boinq.repository;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.boinq.domain.Datasource;
import org.boinq.domain.Track;
import org.boinq.service.MetaInfoService;

public class DatasourceRepositoryImpl implements DatasourceRepositoryExtensions {

	@Inject
	private DatasourceRepository datasourceRepository;
	@Inject
	private MetaInfoService metaInfoService;
	
	@Override
	public Optional<Datasource> findOneWithMeta(Long id) {
		Optional<Datasource> result = datasourceRepository.findOneById(id);
		if (result.isPresent()) {
			for (Track track: result.get().getTracks()) {
				metaInfoService.addMetaInfo(track);
			}
		}
		return result;
	}

	@Override
	public List<Datasource> findAllWithMeta() {
		List<Datasource> result = datasourceRepository.findAll();
		for (Datasource datasource: result) {
			for (Track track: datasource.getTracks()) {
				metaInfoService.addMetaInfo(track);
			}
		}
		return result;
	}

}
