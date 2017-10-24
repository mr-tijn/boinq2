package org.boinq.repository;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.boinq.domain.match.FeatureQuery;
import org.boinq.domain.match.FeatureSelect;
import org.boinq.service.MetaInfoService;

public class FeatureQueryRepositoryImpl implements FeatureQueryRepositoryExtensions {

	
	@Inject
	private FeatureQueryRepository featureQueryRepository;
	@Inject
	private MetaInfoService metaInfoService;

	
	@Override
	public Optional<FeatureQuery> findOneWithMeta(Long id) {
		Optional<FeatureQuery> result = featureQueryRepository.findOneById(id);
		if (result.isPresent()) {
			for (FeatureSelect select: result.get().getSelects()) {
				metaInfoService.addMetaInfo(select.getTrack());
			}
		}
		return result;
	}

	@Override
	public List<FeatureQuery> findAllWithMeta() {
		List<FeatureQuery> result = featureQueryRepository.findAll();
		for (FeatureQuery featureQuery: result) {
			for (FeatureSelect select: featureQuery.getSelects()) {
				metaInfoService.addMetaInfo(select.getTrack());
			}
		}
		return result;
	}

}
