package com.genohm.boinq.web.rest;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.repository.GraphTemplateRepository;
import com.genohm.boinq.service.GraphTemplateRepositoryTestData;
import com.genohm.boinq.web.rest.dto.GraphTemplateDTO;

@RestController
@RequestMapping("/app/rest/")
public class GraphTemplateResource {
	
	@Inject
	private GraphTemplateRepository graphTemplateRepository;

    @RequestMapping(value = "graphtemplate/{gtId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public GraphTemplate get(Principal principal, @PathVariable Long gtId) throws Exception {
     	return graphTemplateRepository
    			.findOneById(gtId)
//    			.map(graphTemplate -> GraphTemplateDTO.create(graphTemplate))
    			.orElseThrow(() -> new RuntimeException("Cannot get graphtemplate "+gtId));
    }
    
    @RequestMapping(value = "graphtemplate/{gtId}",
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public GraphTemplate save(Principal principal, @RequestBody GraphTemplate template) throws Exception {
    	return graphTemplateRepository.save(template);
    }
}
