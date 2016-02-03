package com.genohm.boinq.tools.generators;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureSelect;
import com.genohm.boinq.domain.match.FeatureTypeCriterion;
import com.genohm.boinq.domain.match.LocationCriterion;
import com.genohm.boinq.domain.match.LocationOverlap;
import com.genohm.boinq.domain.match.MatchGOTermCriterion;

public interface QueryGenerator {
	void visit(LocationCriterion lc, GenomicRegion r);
	Boolean check(LocationCriterion lc, GenomicRegion r);
	void visit(FeatureTypeCriterion fc, GenomicRegion r);
	Boolean check(FeatureTypeCriterion fc, GenomicRegion r);
	void visit(LocationOverlap lo, GenomicRegion r);
	Boolean check(LocationOverlap lo, GenomicRegion r);
	void visit(FeatureSelect fs, GenomicRegion r);
	Boolean check(FeatureSelect fs, GenomicRegion r);
	void visit(FeatureQuery fq, GenomicRegion r);
	Boolean check(FeatureQuery fq, GenomicRegion r);
	void visit(MatchGOTermCriterion tc, GenomicRegion r);
	Boolean check(MatchGOTermCriterion tc, GenomicRegion r);
}
