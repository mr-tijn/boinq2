package com.genohm.boinq.domain.jobs.analysis;

import com.genohm.boinq.service.LocalGraphService;

public interface TrackBuildingAnalysis extends Analysis {
	void setLocalGraphService(LocalGraphService localGraphService);
	LocalGraphService getLocalGraphService();
}
