package com.genohm.boinq.domain.match;

import java.util.Set;

import com.genohm.boinq.tools.generators.QueryGenerator;

public interface FeatureQuery {
	Set<FeatureJoin> getJoins();
	Set<FeatureSelect> getSelect();
	
	void accept(QueryGenerator qg);
}
