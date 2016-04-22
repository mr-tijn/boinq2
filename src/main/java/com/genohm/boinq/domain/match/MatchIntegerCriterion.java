package com.genohm.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;

@Entity
@DiscriminatorValue(value=CriteriaDTO.MATCHINTEGER_CRITERIA)
public class MatchIntegerCriterion extends MatchFieldCriterion {
	
	// criterion properties
	@Column(name="min_long")
	private Long minLong; 
	@Column(name="max_long")
	private Long maxLong;
	@Column(name="match_long")
	private Long matchLong;
	
	public Long getMinLong() {
		return minLong;
	}
	public void setMinLong(Long minLong) {
		this.minLong = minLong;
	}
	public Long getMaxLong() {
		return maxLong;
	}
	public void setMaxLong(Long maxLong) {
		this.maxLong = maxLong;
	}
	public Long getMatchLong() {
		return matchLong;
	}
	public void setMatchLong(Long matchLong) {
		this.matchLong = matchLong;
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
