package org.boinq.repository;

import javax.inject.Inject;

import org.boinq.domain.query.QueryGraph;
import org.boinq.web.rest.dto.QueryGraphDTO;

public interface QueryGraphRepositoryExtensions {
	
	@Inject
	
	QueryGraph create(QueryGraphDTO graphDTO);
}
