package com.genohm.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;

@Entity
@DiscriminatorValue(value=CriteriaDTO.MATCHDECIMAL_CRITERIA)
public class MatchDecimalCriterion extends MatchFieldCriterion {
	
	// criterion properties
    @Column(name="min_double")
	private Double minDouble; 
    @Column(name="max_double")
	private Double maxDouble;
    @Column(name="match_double")
    private Double matchDouble; 
    
	public Double getMinDouble() {
		return minDouble;
	}
	public void setMinDouble(Double minDouble) {
		this.minDouble = minDouble;
	}
	public Double getMaxDouble() {
		return maxDouble;
	}
	public void setMaxDouble(Double maxDouble) {
		this.maxDouble = maxDouble;
	}
	public Double getMatchDouble() {
		return matchDouble;
	}
	public void setMatchDouble(Double matchDouble) {
		this.matchDouble = matchDouble;
	}
	@Override
	public void accept(QueryGenerator qg, GenomicRegion region) {
		qg.visit(this, region);
	}

	@Override
	public Boolean check(QueryGenerator qg, GenomicRegion region) {
		return qg.check(this, region);
	}
}
