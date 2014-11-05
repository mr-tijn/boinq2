package com.genohm.boinq.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genohm.boinq.domain.jobs.AsynchronousJob;
import com.genohm.boinq.repository.DatasourceRepository;

public class JobRunner implements Job {

	// Helper class: Mechanism to support instance based job runs
	public static final String IDENTIFIER_JOB = "theJob";
	public static final String IDENTIFIER_DATASOURCE_REPOSITORY = "datasourceRepository";
	public static final String IDENTIFIER_TRIPLEUPLOAD_SERVICE = "tripleUploadService";
	public final Logger log = LoggerFactory.getLogger(JobRunner.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		AsynchronousJob theComp = (AsynchronousJob) context.getJobDetail().getJobDataMap().get(IDENTIFIER_JOB);
		try {
			// get stuff from context
			DatasourceRepository datasourceRepository = (DatasourceRepository) context.getScheduler().getContext().get(IDENTIFIER_DATASOURCE_REPOSITORY);
			TripleUploadService tripleUploadService = (TripleUploadService) context.getScheduler().getContext().get(IDENTIFIER_TRIPLEUPLOAD_SERVICE);
			theComp.setTripleUploadService(tripleUploadService);
			theComp.setDatasourceRepository(datasourceRepository);
			// run
			theComp.execute();
		} catch (Exception e) {
			log.error("Problem while launching computation", e);
			throw new JobExecutionException("Problem while launching computation", e);
		}
	}

}