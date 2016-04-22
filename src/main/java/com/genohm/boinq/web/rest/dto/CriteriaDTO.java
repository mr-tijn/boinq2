package com.genohm.boinq.web.rest.dto;

import com.genohm.boinq.generated.vocabularies.TrackVocab;

public class CriteriaDTO {
	public static final String LOCATION_CRITERIA = "Location";
	public static final String FEATURETYPE_CRITERIA = "FeatureType";
	public static final String MATCHTERM_CRITERIA = "MatchTerm";
	public static final String MATCHINTEGER_CRITERIA = "MatchInteger";
	public static final String MATCHDECIMAL_CRITERIA = "MatchDecimal";
	public static final String MATCHSTRING_CRITERIA = "MatchString";

	public String name;
	public String type;
	
	// Location
	public String contig;
	public Long start;
	public Long end;
	public Boolean strand;
	
	// FeatureType
	public String featureTypeUri;
	public String featureTypeLabel;

	// MatchField
	public String pathExpression;
	public Boolean exactMatch;

	// MatchTerm
//	public String termSourceEndpoint;
//	public String termSourceGraph;
//	public String termSourceRoot;
	public String termUri;
	public String termLabel;

	// MatchDecimal
	public Double minDouble;
	public Double maxDouble;
	
	// MatchInteger
	public Long minLong;
	public Long maxLong;
	
	// MatchString
	public String matchString;
	
	public CriteriaDTO() {}
	
}
