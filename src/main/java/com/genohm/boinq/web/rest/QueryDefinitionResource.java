package com.genohm.boinq.web.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.genohm.boinq.domain.User;
import com.genohm.boinq.domain.jobs.analysis.QueryExecution;
import com.genohm.boinq.domain.query.QueryDefinition;
import com.genohm.boinq.repository.QueryDefinitionRepository;
import com.genohm.boinq.repository.UserRepository;
import com.genohm.boinq.service.AsynchronousJobService;
import com.genohm.boinq.service.GenerateQueryService;
import com.genohm.boinq.web.rest.dto.QueryDefinitionDTO;

@RestController
@RequestMapping("/app/rest/")
public class QueryDefinitionResource {

	@Inject
	private QueryDefinitionRepository queryDefinitionRepository;
	@Inject
	private GenerateQueryService generateQueryService;
	@Inject
	private UserRepository userRepository;
	@Inject
	private AsynchronousJobService jobService;
	
	@RequestMapping(value = "querydefinition/{qdId}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public QueryDefinitionDTO get(Principal principal, @PathVariable Long qdId) {
		return queryDefinitionRepository.findOneById(qdId).map(QueryDefinitionDTO::create).orElseThrow(() -> new RuntimeException("Could not get querydefinition " + qdId));
	}
	
	@RequestMapping(value = "querydefinition/{qdId}", 
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(Principal principal, @PathVariable Long qdId) throws Exception {
		Optional<QueryDefinition> queryDefinition = queryDefinitionRepository.findOneById(qdId);
		if (queryDefinition.isPresent()) {
			if (queryDefinition.get().getOwner() == null || queryDefinition.get().getOwner().getLogin().equals(principal.getName())) {
				queryDefinitionRepository.delete(queryDefinition.get());
			}
		}
	}
	
	@RequestMapping(value = "querydefinition",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<QueryDefinitionDTO> getAll(Principal principal) throws Exception {
	    	return queryDefinitionRepository.findAll().stream().filter(qd -> (qd.getOwner() == null || qd.getOwner().getLogin().equals(principal.getName()))).map(QueryDefinitionDTO::create).collect(Collectors.toList());
	}
	   
	@RequestMapping(value = "querydefinition",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public QueryDefinitionDTO save(Principal principal, @RequestBody QueryDefinitionDTO queryDefinitionDTO) {
		QueryDefinition queryDefinition = queryDefinitionRepository.create(queryDefinitionDTO);
		Optional<User> owner = userRepository.findOneByLogin(principal.getName());
		if (owner.isPresent()) {
			queryDefinition.setOwner(owner.get());
		}
		return QueryDefinitionDTO.create(queryDefinitionRepository.save(queryDefinition));
	}
	
	
    @RequestMapping(value = "querydefinition/{qdId}/start",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void start(Principal principal, @PathVariable Long qdId, @RequestBody int start) throws Exception {
    	if (start != 1) return;
    	Optional<QueryDefinition> queryDefinition = queryDefinitionRepository
    			.findOneById(qdId);
    	if (queryDefinition.isPresent()) {
    		QueryExecution analysis = new QueryExecution(queryDefinition.get());
    		analysis.setName("Execution "+new Date());
    		jobService.add(analysis);
    	}
    }

    
    @RequestMapping(value = "querydefinition/{qdId}/generatequery",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Transactional
    public String generatequery(Principal principal, @PathVariable Long qdId, @RequestBody int start) throws Exception {
    	if (start != 1) return "";
    	Optional<QueryDefinition> queryDefinitionOpt = queryDefinitionRepository
    			.findOneById(qdId);
    	if (queryDefinitionOpt.isPresent()) {
    		QueryDefinition queryDefinition = queryDefinitionOpt.get();
    		String sparqlQuery = generateQueryService.generateQuery(queryDefinition);
    		queryDefinition.setSparqlQuery(sparqlQuery);
    		queryDefinitionRepository.save(queryDefinition);
    		return sparqlQuery;
    	}
    	return "";
    }

    
    @RequestMapping(value = "querydefinition/{qdId}/updatequery",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public void updatequery(Principal principal, @PathVariable Long qdId, @RequestBody String sparqlQuery) throws Exception {
    	Optional<QueryDefinition> queryDefinitionOpt = queryDefinitionRepository
    			.findOneById(qdId);
    	if (queryDefinitionOpt.isPresent()) {
    		QueryDefinition queryDefinition = queryDefinitionOpt.get();
    		queryDefinition.setSparqlQuery(sparqlQuery);
    		queryDefinitionRepository.save(queryDefinition);
    	}
    }


    @RequestMapping(value = "querydefinition/{qdId}/removequery",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public void removequery(Principal principal, @PathVariable Long qdId) {
    	Optional<QueryDefinition> queryDefinitionOpt = queryDefinitionRepository
    			.findOneById(qdId);
    	if (queryDefinitionOpt.isPresent()) {
    		QueryDefinition queryDefinition = queryDefinitionOpt.get();
    		queryDefinition.setSparqlQuery(null);
    		queryDefinitionRepository.save(queryDefinition);
    	}
    }
    
    @RequestMapping(value = "querydefinition/{qdId}/resultfile", 
    	method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable Long qdId) throws IOException {

    	Optional<QueryDefinition> queryDefinition = queryDefinitionRepository.findOneById(qdId);
    	if (queryDefinition.isPresent()) {
    		File file = new File(queryDefinition.get().getTargetFile());
    		Path path = Paths.get(file.getAbsolutePath());
    		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    		return ResponseEntity.ok()
    				.contentLength(file.length())
    				.contentType(MediaType.parseMediaType("application/octet-stream"))
    				.body(resource);
    	} else {
    		// ?
    		return ResponseEntity.badRequest().body(new ByteArrayResource("Could not".getBytes()));
    	}
    }
    
    
}
