package com.genohm.boinq.domain.match;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.genohm.boinq.tools.generators.SPARQLGenerator;
import com.genohm.boinq.web.rest.dto.MatchDTO;

public interface Match {
	public void acceptGenerator(SPARQLGenerator generator, String subjectIdentifier);
	public MatchDTO asDTO();
}