package org.boinq.web.rest.dto;

public class FeatureJoinDTO {
	
	public static final String JOIN_TYPE_OVERLAP = "LocationOverlap";
	public static final String JOIN_TYPE_CONNECT = "Connect";
	
	public String type;
	public int sourceSelectIdx;
	public int targetSelectIdx;
	
	public FeatureConnectorDTO sourceConnector;
	public FeatureConnectorDTO targetConnector;

	public boolean sameStrand;
	
	public FeatureJoinDTO() {}
	
}
