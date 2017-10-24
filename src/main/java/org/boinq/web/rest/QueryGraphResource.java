package org.boinq.web.rest;

import java.security.Principal;

import javax.inject.Inject;

import org.boinq.domain.query.QueryGraph;
import org.boinq.repository.QueryGraphRepository;
import org.boinq.web.rest.dto.QueryGraphDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/app/rest/")
public class QueryGraphResource {

	@Inject
	private QueryGraphRepository queryGraphRepository;
	
	@RequestMapping(value = "querygraph/{qgId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public QueryGraphDTO get(Principal principal, @PathVariable Long qgId) throws RuntimeException {
		return queryGraphRepository.findOneById(qgId).map(queryGraph -> QueryGraphDTO.create(queryGraph))
				.orElseThrow(() -> new RuntimeException("Could not get querygraph " + qgId));
	}
	
	@RequestMapping(value = "querygraph/",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public QueryGraph save(Principal principal, @RequestBody QueryGraphDTO queryGraphDTO) throws RuntimeException {
		QueryGraph queryGraph = queryGraphRepository.create(queryGraphDTO);
		return queryGraphRepository.save(queryGraph);
	}
	
}

