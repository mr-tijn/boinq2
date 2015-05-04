package com.genohm.boinq.service;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.genohm.boinq.domain.jobs.AsynchronousJob;

public class JobRunner implements InterruptableJob {

	// Helper class: Mechanism to support instance based job runs
	public static final String IDENTIFIER_JOB = "theJob";
	public static final String IDENTIFIER_SPRINGCONTEXT = "springContext";
	public final Logger log = LoggerFactory.getLogger(JobRunner.class);
	
	private AsynchronousJob theComp;
	private ApplicationContext applicationContext;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		theComp = (AsynchronousJob) context.getJobDetail().getJobDataMap().get(IDENTIFIER_JOB);
		try {
			// wire beans
			applicationContext = (ApplicationContext) context.getScheduler().getContext().get(IDENTIFIER_SPRINGCONTEXT); 
			AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
			factory.autowireBean(theComp);
			theComp.execute();
		} catch (Exception e) {
			log.error("Problem while launching computation", e);
			throw new JobExecutionException("Problem while launching computation", e);
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		try {
			theComp.kill();
		} catch (Exception e) {
			throw new UnableToInterruptJobException(e);
		}
		
	}
	
}