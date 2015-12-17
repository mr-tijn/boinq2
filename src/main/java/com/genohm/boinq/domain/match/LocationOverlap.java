package com.genohm.boinq.domain.match;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.FeatureJoinDTO;

@Entity
@DiscriminatorValue(value=FeatureJoinDTO.JOIN_TYPE_OVERLAP)
public class LocationOverlap extends FeatureJoin {
 
	public LocationOverlap() {}
	
	@Override
	public void accept(QueryGenerator qg, GenomicRegion r) {
		qg.visit(this, r);
	}
	
	public FeatureJoinDTO createDTO() {
		FeatureJoinDTO result = super.createDTO();
		result.type = FeatureJoinDTO.JOIN_TYPE_OVERLAP;
		return result;
	}
	
}
