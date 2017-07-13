package com.genohm.boinq.repository;

import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.web.rest.dto.GraphTemplateDTO;

public interface GraphTemplateRepositoryExtensions {
	public GraphTemplate create(GraphTemplateDTO templateDTO);
}
