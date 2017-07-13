package com.genohm.boinq.domain.jobs;

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
	public static int JOB_STATUS_INTERRUPTED = 5;
	
	String getName();
	void setName(String name);
	int getStatus();
	String getDescription();
	String getErrorDescription();
	Long getDuration();
	void execute();
	void kill();
	
}
