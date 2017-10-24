package org.boinq.web.rest;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.boinq.domain.jobs.AsynchronousJob;
import org.boinq.security.AuthoritiesConstants;
import org.boinq.service.AsynchronousJobService;
import org.boinq.web.rest.dto.JobDTO;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/app")
public class JobResource {

	private static Logger log = LoggerFactory.getLogger(JobResource.class);
	
	@Inject
	private AsynchronousJobService jobService;
	
    @RequestMapping(value = "/rest/jobs",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<List<JobDTO>> getList() {
    	List<JobDTO> jobs = new LinkedList<JobDTO>();
    	try {
    		for (AsynchronousJob job: jobService.listJobs()) {
    			jobs.add(new JobDTO(job));
    		}
    		return new ResponseEntity<List<JobDTO>>(jobs, HttpStatus.OK);
    	} catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @RequestMapping(value = "rest/jobs/cancel", 
    		method = RequestMethod.PUT,
    		produces = "application/json")
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> cancelJob(Principal principal, @RequestBody String jobName) {
    	try {
    		jobService.kill(jobName);
    		return new ResponseEntity<String>("Job deleted", HttpStatus.OK);
    	} catch (SchedulerException e) {
    		log.error("Could not kill job", e);
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

}
