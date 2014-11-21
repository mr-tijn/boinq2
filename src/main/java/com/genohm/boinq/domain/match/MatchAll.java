package com.genohm.boinq.domain.match;

import java.util.LinkedHashSet;
import java.util.Set;

import com.genohm.boinq.tools.generators.SPARQLGenerator;
import com.genohm.boinq.web.rest.dto.MatchDTO;

public class MatchAll implements Match {

	private Set<Match> nodes = new LinkedHashSet<Match>();
	
	public MatchAll(MatchDTO matchDTO) throws Exception {
		for (MatchDTO node: matchDTO.nodes) {
			nodes.add(MatchFactory.fromDTO(node));
		}
	}
		
	@Override
	public void acceptGenerator(SPARQLGenerator generator, String subjectIdentifier) {
		generator.visitMatch(this, subjectIdentifier);
	}

	@Override
	public MatchDTO asDTO() {
		MatchDTO matchDTO = new MatchDTO();
		matchDTO.type = MatchDTO.MATCH_ALL;
		for (Match node: nodes) {
			matchDTO.nodes.add(node.asDTO());
		}
		return null;
	}

	public Set<Match> getNodes() {
		return nodes;
	}

}
