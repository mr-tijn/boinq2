package com.genohm.boinq.domain.jobs.analysis;


public interface TrackBuildingAnalysis extends Analysis {
	void setTarget(String endpoint, String graph);
	void setMeta(String endpoint, String graph);
}
