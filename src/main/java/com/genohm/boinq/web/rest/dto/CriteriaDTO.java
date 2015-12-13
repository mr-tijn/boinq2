package com.genohm.boinq.web.rest.dto;

public class CriteriaDTO {
	public static final String LOCATION_CRITERIA = "Location";
	public static final String FEATURETYPE_CRITERIA = "FeatureType";
	
	public String type;
	
	// Location
	public String contig;
	public Long start;
	public Long end;
	public Boolean strand;
	
	// FeatureType
	public String featureTypeUri;
	public String featureTypeLabel;
	
}
