package com.genohm.boinq.web.rest;


import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.genohm.boinq.service.QueryBuilderService;
import com.genohm.boinq.web.rest.dto.MatchDTO;
import com.genohm.boinq.web.rest.dto.QueryDTO;


@RestController
@RequestMapping("/app")
public class QueryBuilderResource {

 	private final Logger log = LoggerFactory.getLogger(QueryBuilderResource.class);

	@Inject
	private QueryBuilderService queryBuilderService;
	
    @RequestMapping(value = "/rest/querybuilder/rootNodesQuery",
            		method = RequestMethod.GET,
            		produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<QueryDTO> getRootNodesQuery() {
    	try {
    		QueryDTO result = new QueryDTO(queryBuilderService.getRootNodes(),"SPARQL1.1");
    		return new ResponseEntity<>(result, HttpStatus.OK);
    	} catch (Exception e) {
    		log.error("Could not get rootNodes Query",e);
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

    @RequestMapping(value = "/rest/querybuilder/childNodesQuery",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getChildNodesQuery(@RequestParam String parentUri) {
    	try {
    		String result = queryBuilderService.getChildNodes(parentUri);
    		return new ResponseEntity<String>(result, HttpStatus.OK);
    	} catch (Exception e) {
    		log.error("Could not get rootNodes Query",e);
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @RequestMapping(value = "/rest/querybuilder/filteredTreeQuery",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFilteredTreeQuery(@RequestParam String filter) {
    	try {
    		String result = queryBuilderService.getFilteredTree(filter);
    		return new ResponseEntity<String>(result, HttpStatus.OK);
    	} catch (Exception e) {
    		log.error("Could not get filteredTree Query",e);
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

    @RequestMapping(value = "/rest/querybuilder/insertQuery",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInsertQuery(@RequestParam String graphUri, @RequestParam String subject, @RequestParam String predicate, @RequestParam String object) {
    	try {
    		String result = queryBuilderService.insertStatement(graphUri, subject, predicate, object);
    		return new ResponseEntity<String>(result, HttpStatus.OK);
    	} catch (Exception e) {
    		log.error("Could not create insert statement",e);
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}

    @RequestMapping(value = "/rest/querybuilder/from_match",
    		method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_JSON_VALUE,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<QueryDTO> getQueryFromMatch(@RequestBody MatchDTO matchDTO) {
    	try {
    		QueryDTO result = new QueryDTO(queryBuilderService.getQueryFromMatch(matchDTO),"SPARQL11");
    		return new ResponseEntity<>(result, HttpStatus.OK);
    	} catch (Exception e) {
    		log.error("Could not create insert statement",e);
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}


}
