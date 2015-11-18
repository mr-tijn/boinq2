package com.genohm.boinq.web.rest;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.domain.match.FeatureQueryFactory;
import com.genohm.boinq.repository.FeatureQueryRepository;
import com.genohm.boinq.web.rest.dto.DatasourceDTO;
import com.genohm.boinq.web.rest.dto.FeatureQueryDTO;

@RestController
@RequestMapping("/app")
public class FeatureQueryResource {

	@Inject
	private FeatureQueryRepository featureQueryResource;
	
    @RequestMapping(value = "/rest/featurequery",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(Principal principal, @RequestBody FeatureQueryDTO featureQueryDTO) throws Exception {
    	FeatureQuery fq = FeatureQueryFactory.createFeatureQuery(featureQueryDTO);
    	
    }
    

	
}
