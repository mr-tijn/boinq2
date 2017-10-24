package org.boinq.repository;

import java.util.List;
import java.util.Optional;

import org.boinq.domain.User;
import org.boinq.domain.match.FeatureQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureQueryRepository extends JpaRepository<FeatureQuery, Long>, FeatureQueryRepositoryExtensions {

	Optional<FeatureQuery> findOneById(Long id);
	Optional<List<FeatureQuery>> findByOwner(User owner);
}
