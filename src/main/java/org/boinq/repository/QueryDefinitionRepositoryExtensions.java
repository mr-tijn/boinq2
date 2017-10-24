package org.boinq.repository;

import org.boinq.domain.query.QueryDefinition;
import org.boinq.web.rest.dto.QueryDefinitionDTO;

public interface QueryDefinitionRepositoryExtensions {
	public QueryDefinition create(QueryDefinitionDTO definitionDTO);
}
