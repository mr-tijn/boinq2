package com.genohm.boinq.web.rest;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.jobs.AsynchronousJob;
import com.genohm.boinq.service.AsynchronousJobService;
import com.genohm.boinq.web.rest.dto.JobDTO;

@RestController
@RequestMapping("/app")
public class JobResource {

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

}
