package com.genohm.boinq.repository;

import com.genohm.boinq.domain.query.QueryDefinition;
import com.genohm.boinq.web.rest.dto.QueryDefinitionDTO;

public interface QueryDefinitionRepositoryExtensions {
	public QueryDefinition create(QueryDefinitionDTO definitionDTO);
}
