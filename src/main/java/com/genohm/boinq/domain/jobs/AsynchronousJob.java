package com.genohm.boinq.domain.jobs;

import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.service.TripleUploadService;

/**
 * @author martijn
 * Convenience interface for asynchronous jobs to be executed on the server. 
 */
public interface AsynchronousJob {

	
	public static int JOB_STATUS_PENDING = 0;
	public static int JOB_STATUS_COMPUTING = 1;
	public static int JOB_STATUS_SUCCESS = 2;
	public static int JOB_STATUS_ERROR = 3;
	public static int JOB_STATUS_UNKNOWN = 4;
	
	void setDatasourceRepository(DatasourceRepository datasourceRepository);
	DatasourceRepository getDatasourceRepository();
	void setTripleUploadService(TripleUploadService tripleUploadService);
	TripleUploadService getTripleUploadService();
	String getName();
	void setName(String name);
	int getStatus();
	String getDescription();
	void execute();
	
}
