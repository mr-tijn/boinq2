package org.boinq.web.rest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

import org.boinq.domain.query.GraphTemplate;
import org.boinq.repository.GraphTemplateRepository;
import org.boinq.web.rest.dto.GraphTemplateDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/app/rest/")
public class GraphTemplateResource {
	
	@Inject
	private GraphTemplateRepository graphTemplateRepository;

    @RequestMapping(value = "graphtemplate/{gtId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public GraphTemplateDTO get(Principal principal, @PathVariable Long gtId) throws Exception {
    	Optional<GraphTemplate> graphTemplate = graphTemplateRepository.findOneById(gtId);
    	if (graphTemplate.isPresent()) {
    		GraphTemplateDTO result = GraphTemplateDTO.create(graphTemplate.get()); 
    		return result;
    	} else {
    		throw new RuntimeException("Cannot get graphtemplate "+gtId);
    	}
    }
    
    
    @RequestMapping(value = "graphtemplate",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GraphTemplate> getAll(Principal principal) throws Exception {
    	return graphTemplateRepository
    			.findAll();
    }
    
    @RequestMapping(value = "graphtemplate/{gtId}",
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public GraphTemplate save(Principal principal, @RequestBody GraphTemplateDTO template) throws Exception {
    	GraphTemplate gt = graphTemplateRepository.create(template);
    	return graphTemplateRepository.deepsave(gt);
    }
}
