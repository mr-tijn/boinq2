package com.genohm.boinq.repository;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureSelect;
import com.genohm.boinq.service.MetaInfoService;

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
