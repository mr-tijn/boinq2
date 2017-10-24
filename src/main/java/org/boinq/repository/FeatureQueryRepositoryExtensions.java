package org.boinq.repository;

import java.util.List;
import java.util.Optional;

import org.boinq.domain.match.FeatureQuery;

public interface FeatureQueryRepositoryExtensions {
	Optional<FeatureQuery> findOneWithMeta(Long id);
	List<FeatureQuery> findAllWithMeta();
}
