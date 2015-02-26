package com.genohm.boinq.domain.jobs.analysis;

import com.genohm.boinq.domain.jobs.AsynchronousJob;
import com.genohm.boinq.service.SPARQLClientService;

public interface Analysis extends AsynchronousJob {

	void setSPARQLClientService(SPARQLClientService client);
	SPARQLClientService getSPARQLClientService();
	
}
