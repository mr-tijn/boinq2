package com.genohm.boinq.web.rest.dto;

import java.util.Set;

public class MatchDTO {
	public static final String MATCH_ALL = "MatchAll";	
	public static final String MATCH_ANY = "MatchAny";
	public static final String MATCH_FIELD = "MatchField";
	public static final String MATCH_TYPE = "MatchType";
	public static final String MATCH_LOCATION = "MatchLocation";
	public static final String MATCH_OVERLAP = "MatchOverlap";

	public String name;
	public String type;
	
	// MatchAll, MatchAny
	public Set<MatchDTO> nodes;

	// MatchLocation
	public String contig;
	public Long start;
	public Long end;
	public Boolean strand;

	public MatchDTO() {}
}
