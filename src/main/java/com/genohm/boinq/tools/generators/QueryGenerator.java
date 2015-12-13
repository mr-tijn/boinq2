package com.genohm.boinq.tools.generators;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureSelect;
import com.genohm.boinq.domain.match.FeatureTypeCriterion;
import com.genohm.boinq.domain.match.LocationCriterion;
import com.genohm.boinq.domain.match.LocationOverlap;

public interface QueryGenerator {
	void visit(LocationCriterion lc, GenomicRegion r);
	void visit(FeatureTypeCriterion fc, GenomicRegion r);
	void visit(LocationOverlap lo, GenomicRegion r);
	void visit(FeatureSelect fs, GenomicRegion r);
	void visit(FeatureQuery fq, GenomicRegion r);
}
