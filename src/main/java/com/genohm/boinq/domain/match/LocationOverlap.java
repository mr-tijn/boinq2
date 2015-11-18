package com.genohm.boinq.domain.match;

import javax.persistence.DiscriminatorValue;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.FeatureJoinDTO;

@DiscriminatorValue(value=FeatureJoinDTO.JOIN_TYPE_OVERLAP)
public class LocationOverlap extends FeatureJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

	public LocationOverlap() {}
	
	@Override
	public void accept(QueryGenerator qg, GenomicRegion r) {
		qg.visit(this, r);
	}
	
}
