package org.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.boinq.domain.GenomicRegion;
import org.boinq.tools.generators.QueryGenerator;
import org.boinq.web.rest.dto.FeatureJoinDTO;

@Entity
@DiscriminatorValue(value=FeatureJoinDTO.JOIN_TYPE_OVERLAP)
public class LocationOverlap extends FeatureJoin {
 
	@Column(name="same_strand")
	private Boolean sameStrand;
	public LocationOverlap() {}
	
	public Boolean getSameStrand() {
		return sameStrand;
	}

	public void setSameStrand(Boolean sameStrand) {
		this.sameStrand = sameStrand;
	}

	@Override
	public Boolean check(QueryGenerator qg, GenomicRegion r) {
		return (qg.check(this, r));
	}
	
	@Override
	public void accept(QueryGenerator qg, GenomicRegion r) {
		qg.visit(this, r);
	}
	
	public FeatureJoinDTO createDTO() {
		FeatureJoinDTO result = super.createDTO();
		result.sameStrand = this.sameStrand;
		result.type = FeatureJoinDTO.JOIN_TYPE_OVERLAP;
		return result;
	}
	
}
