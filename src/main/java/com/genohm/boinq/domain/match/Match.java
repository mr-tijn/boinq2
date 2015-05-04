package com.genohm.boinq.domain.match;

import com.genohm.boinq.tools.generators.SPARQLGenerator;
import com.genohm.boinq.web.rest.dto.MatchDTO;

public interface Match {
	public void acceptGenerator(SPARQLGenerator generator, String subjectIdentifier);
	public MatchDTO asDTO();
}