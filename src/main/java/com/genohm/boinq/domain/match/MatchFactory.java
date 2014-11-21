package com.genohm.boinq.domain.match;

import com.genohm.boinq.web.rest.dto.MatchDTO;

public class MatchFactory {
	public static Match fromDTO(MatchDTO matchDTO) throws Exception {
		switch (matchDTO.type) {
		case MatchDTO.MATCH_ALL: return new MatchAll(matchDTO);
		case MatchDTO.MATCH_LOCATION: return new MatchLocation(matchDTO);
		}
		throw new Exception("Cannot handle type "+matchDTO.type);
	}
}
