package com.genohm.boinq.web.rest;

import java.security.Principal;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureQueryFactory;
import com.genohm.boinq.repository.FeatureQueryRepository;
import com.genohm.boinq.web.rest.dto.FeatureQueryDTO;

@RestController
@RequestMapping("/app")
public class FeatureQueryResource {

	@Inject
	private FeatureQueryRepository featureQueryRepository;
	@Inject
	private FeatureQueryFactory featureQueryFactory;
	
    @RequestMapping(value = "/rest/featurequery",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(Principal principal, @RequestBody FeatureQueryDTO featureQueryDTO) throws Exception {
    	FeatureQuery fq = featureQueryFactory.createFeatureQuery(featureQueryDTO);
    	featureQueryRepository.save(fq);
    }
    
    @RequestMapping(value = "/rest/featurequery",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FeatureQueryDTO> get(Principal principal, @RequestParam Long fqId) throws Exception {
    	return featureQueryRepository.findOneById(fqId)

    			.map((FeatureQuery fq) -> new ResponseEntity<>(fq.createDTO(), HttpStatus.OK))
    		.orElseThrow(() -> new Exception("Could not find FeatureQuery"));
    }
}
