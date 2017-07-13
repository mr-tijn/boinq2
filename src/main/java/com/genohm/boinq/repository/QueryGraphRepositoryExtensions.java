package com.genohm.boinq.repository;

import javax.inject.Inject;

import com.genohm.boinq.domain.query.QueryGraph;
import com.genohm.boinq.web.rest.dto.QueryGraphDTO;

public interface QueryGraphRepositoryExtensions {
	
	@Inject
	
	QueryGraph create(QueryGraphDTO graphDTO);
}
