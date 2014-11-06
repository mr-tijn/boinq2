package com.genohm.boinq.web.rest;


import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genohm.boinq.service.QueryBuilderService;


@RestController
@RequestMapping("/app")
public class QueryBuilderResource {

 	private final Logger log = LoggerFactory.getLogger(QueryBuilderResource.class);

	@Inject
	private QueryBuilderService queryBuilderService;
	
    @RequestMapping(value = "/rest/querybuilder/rootNodesQuery",
            		method = RequestMethod.GET,
            		produces = "application/json")
    public ResponseEntity<String> getRootNodesQuery() {
    	try {
    		String result = queryBuilderService.getRootNodes();
    		return new ResponseEntity<String>(result, HttpStatus.OK);
    	} catch (Exception e) {
    		log.error("Could not get rootNodes Query",e);
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

    @RequestMapping(value = "/rest/querybuilder/childNodesQuery",
    		method = RequestMethod.GET,
    		produces = "application/json")
    public ResponseEntity<String> getChildNodesQuery(@RequestParam String parentUri) {
    	try {
    		String result = queryBuilderService.getChildNodes(parentUri);
    		return new ResponseEntity<String>(result, HttpStatus.OK);
    	} catch (Exception e) {
    		log.error("Could not get rootNodes Query",e);
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

}
