package com.genohm.boinq.domain.match;

import java.util.Set;

import com.genohm.boinq.tools.generators.QueryGenerator;

public interface FeatureSelect {
	Set<FeatureSelectCriteria> getCriteria();
	void addCriteria(FeatureSelectCriteria criteria);
	
	void accept(QueryGenerator qg);
}
