package com.genohm.boinq.web.rest.dto;

public class FeatureJoinDTO {
	
	public static final String JOIN_TYPE_OVERLAP = "LocationOverlap";
	
	public String type;
	public FeatureSelectDTO sourceSelect;
	public FeatureSelectDTO targetSelect;

}
