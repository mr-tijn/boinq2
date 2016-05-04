package com.genohm.boinq.domain.match;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;

public class Connect extends FeatureJoin {

	private FeatureConnector sourceConnector;
	private FeatureConnector targetConnector;
	
	@Override
	public Boolean check(QueryGenerator qg, GenomicRegion region) {
		return qg.check(this, region);
	}

	@Override
	public void accept(QueryGenerator qg, GenomicRegion region) {
		qg.visit(this, region);
	}

	public FeatureConnector getSourceConnector() {
		return sourceConnector;
	}

	public void setSourceConnector(FeatureConnector sourceConnector) {
		this.sourceConnector = sourceConnector;
	}

	public FeatureConnector getTargetConnector() {
		return targetConnector;
	}

	public void setTargetConnector(FeatureConnector targetConnector) {
		this.targetConnector = targetConnector;
	}
	
	

}
