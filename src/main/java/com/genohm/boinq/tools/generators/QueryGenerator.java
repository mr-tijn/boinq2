package com.genohm.boinq.tools.generators;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureSelect;
import com.genohm.boinq.domain.match.FeatureTypeCriterion;
import com.genohm.boinq.domain.match.Connect;
import com.genohm.boinq.domain.match.LocationCriterion;
import com.genohm.boinq.domain.match.LocationOverlap;
import com.genohm.boinq.domain.match.MatchDecimalCriterion;
import com.genohm.boinq.domain.match.MatchIntegerCriterion;
import com.genohm.boinq.domain.match.MatchStringCriterion;
import com.genohm.boinq.domain.match.MatchTermCriterion;

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
	void visit(MatchTermCriterion tc, GenomicRegion r);
	Boolean check(MatchTermCriterion tc, GenomicRegion r);
	void visit(MatchIntegerCriterion tc, GenomicRegion r);
	Boolean check(MatchIntegerCriterion tc, GenomicRegion r);
	void visit(MatchDecimalCriterion tc, GenomicRegion r);
	Boolean check(MatchDecimalCriterion tc, GenomicRegion r);
	void visit(MatchStringCriterion tc, GenomicRegion r);
	Boolean check(MatchStringCriterion tc, GenomicRegion r);
	Boolean check(Connect idMatch, GenomicRegion region);
	void visit(Connect idMatch, GenomicRegion region);
}
