package com.genohm.boinq.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.genohm.boinq.domain.Track;
import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.service.QueryBuilderService;
import com.genohm.boinq.web.rest.dto.QueryDTO;

@RestController
@RequestMapping("/app")
public class QueryBuilderResource {

	private final Logger log = LoggerFactory.getLogger(QueryBuilderResource.class);

	@Inject
	private QueryBuilderService queryBuilderService;
	@Inject
	private TrackRepository trackRepository;

	@RequestMapping(value = "/rest/querybuilder/rootNodesQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<QueryDTO> getRootNodesQuery() {
		try {
			QueryDTO result = new QueryDTO(queryBuilderService.getRootNodes(), "SPARQL1.1");
			return new ResponseEntity<QueryDTO>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not get rootNodes Query", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/querybuilder/childNodesQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<QueryDTO> getChildNodesQuery(@RequestParam String parentUri) {
		try {
			QueryDTO result = new QueryDTO(queryBuilderService.getChildNodes(parentUri), "SPARQL1.1");
			return new ResponseEntity<QueryDTO>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not get rootNodes Query", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/querybuilder/filteredTreeQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<QueryDTO> getFilteredTreeQuery(@RequestParam String filter) {
		try {
			QueryDTO result = new QueryDTO(queryBuilderService.getFilteredTree(filter), "SPARQL1.1");
			return new ResponseEntity<QueryDTO>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not get filteredTree Query", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/querybuilder/mappingQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<QueryDTO> getMappingQuery() {
		try {
			QueryDTO result = new QueryDTO(queryBuilderService.getMappings(), "SPARQL1.1");
			return new ResponseEntity<QueryDTO>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not get mapping Query", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/querybuilder/assemblyQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<QueryDTO> getAssemblyQuery(@RequestParam String speciesName) {
		try {
			QueryDTO result = new QueryDTO(queryBuilderService.getAssembly(speciesName), "SPARQL1.1");
			return new ResponseEntity<QueryDTO>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not get assembly Query", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/rest/querybuilder/referenceQuery", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<QueryDTO> getReferenceQuery(@RequestParam String assemblyUri) {
		try {
			QueryDTO result = new QueryDTO(queryBuilderService.getReferencesForAssembly(assemblyUri), "SPARQL1.1");
			return new ResponseEntity<QueryDTO>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not get references for assembly", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/querybuilder/insertQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> getInsertQuery(@RequestParam String graphUri,
			@RequestParam String subject, @RequestParam String predicate, @RequestParam String object) {
		try {
			String result = queryBuilderService.insertStatement(graphUri, subject, predicate, object);
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not create insert statement", e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/querybuilder/featureTypesQuery", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getFeatureTypesQuery(@RequestParam Long trackId) {
		return trackRepository.findOneById(trackId).filter((Track t) -> null != t.getGraphName())
				.map((Track t) -> queryBuilderService.findFeatureTypes(t.getGraphName()))
				.map((String s) -> new ResponseEntity<String>(s, HttpStatus.OK))
				.orElse(new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR));
		// if (track == null) return new ResponseEntity<>("Invalid track
		// Id",HttpStatus.INTERNAL_SERVER_ERROR);
		// if (track.getGraphName() == null || track.getGraphName().length()
		// ==0) return new ResponseEntity<String>("Track doesn't have a graph
		// name",HttpStatus.INTERNAL_SERVER_ERROR);
		// String result =
		// queryBuilderService.findFeatureTypes(track.getGraphName());
		// return new ResponseEntity<>(result,HttpStatus.OK);

	}

}
