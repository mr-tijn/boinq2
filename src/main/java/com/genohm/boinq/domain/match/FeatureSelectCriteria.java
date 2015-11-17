package com.genohm.boinq.domain.match;

import com.genohm.boinq.tools.generators.QueryGenerator;

public interface FeatureSelectCriteria {
	void accept(QueryGenerator qg);
}
