package com.genohm.boinq.domain.match;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;

public class MatchGOTermCriterion extends FeatureSelectCriterion {

	private String termSourceEndpoint;
	private String termSourceGraph;
	private String termSourceRoot;
	
	@Override
	public void accept(QueryGenerator qg, GenomicRegion region) {
		qg.visit(this, region);
		
	}

	@Override
	public Boolean check(QueryGenerator qg, GenomicRegion region) {
		return qg.check(this, region);
	}

}
