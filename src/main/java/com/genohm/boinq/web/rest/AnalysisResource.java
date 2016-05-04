package com.genohm.boinq.web.rest;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.repository.AnalysisRepository;
import com.genohm.boinq.web.rest.dto.AnalysisDTO;

@RestController
@RequestMapping("/app")
public class AnalysisResource {
	   @Inject
	   private AnalysisRepository analysisRepository;

	    @RequestMapping(value = "/rest/analysis",
	            method = RequestMethod.GET,
	            produces = "application/json")
	    @Timed
	    public List<AnalysisDTO> get(Principal principal) {
	    	return analysisRepository.findAll().stream().map(ga -> ga.createDTO()).collect(Collectors.toList());
	    }

}
