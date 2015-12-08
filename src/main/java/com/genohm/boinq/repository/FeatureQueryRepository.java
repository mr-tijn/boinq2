package com.genohm.boinq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.User;
import com.genohm.boinq.domain.match.FeatureQuery;

public interface FeatureQueryRepository extends JpaRepository<FeatureQuery, Long> {

	Optional<FeatureQuery> findOneById(Long id);
	Optional<List<FeatureQuery>> findByOwner(User owner);
}
