package org.boinq.domain.match;

import javax.persistence.Column;

import org.boinq.domain.GenomicRegion;
import org.boinq.tools.generators.QueryGenerator;

public class MatchStringCriterion extends MatchFieldCriterion {
	
	
	// criterion properties
    @Column(name="match_string")
	private String matchString; 

	public String getMatchString() {
		return matchString;
	}

	public void setMatchString(String matchString) {
		this.matchString = matchString;
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
