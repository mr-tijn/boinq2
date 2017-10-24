package org.boinq.web.rest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.boinq.domain.jobs.analysis.SlidingWindowFeatureSelection;
import org.boinq.domain.match.FeatureQuery;
import org.boinq.domain.match.FeatureQueryFactory;
import org.boinq.repository.AnalysisRepository;
import org.boinq.repository.FeatureQueryRepository;
import org.boinq.repository.UserRepository;
import org.boinq.service.AsynchronousJobService;
import org.boinq.web.rest.dto.FeatureQueryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/app")
public class FeatureQueryResource {

	@Inject
	private FeatureQueryRepository featureQueryRepository;
	@Inject
	private FeatureQueryFactory featureQueryFactory;
	@Inject
	private UserRepository userRepository;
	@Inject
	private AsynchronousJobService jobService;
	@Inject
	private AnalysisRepository analysisRepository;
	
    @RequestMapping(value = "/rest/featurequery",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(Principal principal, @RequestBody FeatureQueryDTO featureQueryDTO) throws Exception {
    	FeatureQuery fq = featureQueryFactory.createFeatureQuery(featureQueryDTO);
    	featureQueryRepository.save(fq);
    }
    
    @RequestMapping(value= "/rest/featurequery/{fqId}",
    		method = RequestMethod.DELETE)
    public void delete(Principal principal, @PathVariable Long fqId) {
    	Optional<FeatureQuery> fq = featureQueryRepository
    			.findOneById(fqId)
    			.filter((FeatureQuery f) -> f.getOwner().getLogin().equals(principal.getName()));
    	if (fq.isPresent()) {
    		featureQueryRepository.delete(fq.get());
    	}
    }
    
    @RequestMapping(value = "/rest/featurequery/{fqId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureQueryDTO> get(Principal principal, @PathVariable Long fqId) throws Exception {
    	return featureQueryRepository
    			.findOneById(fqId)
    			.filter((FeatureQuery fq) -> fq.getOwner().getLogin().equals(principal.getName()))
    			.map((FeatureQuery fq) -> new ResponseEntity<>(fq.createDTO(), HttpStatus.OK))
    			.orElseThrow(() -> new Exception("Could not find FeatureQuery"));
    }
    
    @RequestMapping(value = "/rest/featurequery",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FeatureQueryDTO>> getAll(Principal principal) throws Exception {
    	return userRepository
    			.findOneByLogin(principal.getName())
    			.flatMap(owner -> featureQueryRepository.findByOwner(owner))
    			.map(listOfFeatureQueries -> listOfFeatureQueries.stream()
    					.map(FeatureQuery::createDTO)
    					.collect(Collectors.toList()))
    			.map(listOfFeatureQueryDTO -> new ResponseEntity<>(listOfFeatureQueryDTO, HttpStatus.OK))
    			.orElseThrow(() -> new Exception("Could not find FeatureQueries"));
    }

    @RequestMapping(value = "/rest/featurequery/{fqId}/start",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void start(Principal principal, @PathVariable Long fqId, @RequestBody int start) throws Exception {
    	if (start != 1) return;
    	Optional<FeatureQuery> featureQuery = featureQueryRepository
    			.findOneWithMeta(fqId)
    			.filter(fq -> fq.getOwner().getLogin().equals(principal.getName()));
    	if (featureQuery.isPresent()) {
    		SlidingWindowFeatureSelection analysis = new SlidingWindowFeatureSelection(featureQuery.get());
    		jobService.add(new SlidingWindowFeatureSelection(featureQuery.get()));
    	}
    }

    
}
