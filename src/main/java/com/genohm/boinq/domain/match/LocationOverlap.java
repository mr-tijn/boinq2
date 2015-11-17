package com.genohm.boinq.domain.match;

import com.genohm.boinq.tools.generators.QueryGenerator;

public class LocationOverlap implements FeatureJoin {

	@Override
	public void accept(QueryGenerator qg) {
		qg.visit(this);
	}
	
}
