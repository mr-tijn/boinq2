package org.boinq.service;


import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.boinq.domain.jobs.AsynchronousJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AsynchronousJobService {

	@Inject //where should this be coming from ?
	private Scheduler mainScheduler;
	
	@Inject
	private ApplicationContext applicationContext;
	
	
	@PostConstruct
	private void init() throws SchedulerException {
		// put application context in job context
		mainScheduler.getContext().put(JobRunner.IDENTIFIER_SPRINGCONTEXT, applicationContext);
	}

	public void add(AsynchronousJob job) throws SchedulerException {
		JobDataMap map = new JobDataMap();
		map.put(JobRunner.IDENTIFIER_JOB, job);
		JobDetail runJob = newJob(JobRunner.class)
				.withDescription(job.getDescription())
				.withIdentity(job.getName())
				.usingJobData(map)
				.build();
		Trigger runTrigger = newTrigger()
				.startNow()
				.build();
		mainScheduler.scheduleJob(runJob,runTrigger);
	}
	
	public void kill(String jobName) throws SchedulerException {
		mainScheduler.interrupt(new JobKey(jobName));
	}
	
	public List<AsynchronousJob> listJobs() throws SchedulerException {
		List<AsynchronousJob> jobs = new LinkedList<AsynchronousJob>(); 
		List<JobExecutionContext> contexts = mainScheduler.getCurrentlyExecutingJobs();
		for (JobExecutionContext context: contexts) {
			AsynchronousJob job = (AsynchronousJob) context.getJobDetail().getJobDataMap().get(JobRunner.IDENTIFIER_JOB);
			
			jobs.add(job);
		}
		return jobs;
	}

}