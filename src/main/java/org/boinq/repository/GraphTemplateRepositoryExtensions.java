package org.boinq.repository;

import org.boinq.domain.query.GraphTemplate;
import org.boinq.web.rest.dto.GraphTemplateDTO;

public interface GraphTemplateRepositoryExtensions {
	public GraphTemplate create(GraphTemplateDTO templateDTO);
	public GraphTemplate deepsave(GraphTemplate template);
}
