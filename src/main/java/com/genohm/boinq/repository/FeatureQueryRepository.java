package com.genohm.boinq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genohm.boinq.domain.match.FeatureQuery;

public interface FeatureQueryRepository extends JpaRepository<FeatureQuery, Long> {

}
