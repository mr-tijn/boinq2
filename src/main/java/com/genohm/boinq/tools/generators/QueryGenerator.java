package com.genohm.boinq.tools.generators;

import com.genohm.boinq.domain.match.FeatureSelectImpl;
import com.genohm.boinq.domain.match.LocationCriteria;
import com.genohm.boinq.domain.match.LocationOverlap;

public interface QueryGenerator {
	void visit(LocationCriteria lc);
	void visit(LocationOverlap lo);
	void visit(FeatureSelectImpl fs);
}
