package org.boinq.tools.generators;

import org.boinq.domain.GenomicRegion;
import org.boinq.domain.match.Connect;
import org.boinq.domain.match.FeatureQuery;
import org.boinq.domain.match.FeatureSelect;
import org.boinq.domain.match.FeatureTypeCriterion;
import org.boinq.domain.match.LocationCriterion;
import org.boinq.domain.match.LocationOverlap;
import org.boinq.domain.match.MatchDecimalCriterion;
import org.boinq.domain.match.MatchIntegerCriterion;
import org.boinq.domain.match.MatchStringCriterion;
import org.boinq.domain.match.MatchTermCriterion;

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
