package org.boinq.web.rest.dto;

import java.util.Map;

public class AnalysisDTO {
	public static int TYPE_FEATURESELECTION = 0;
	public static int TYPE_QUERYTOTABLE = 1;
	public static int TYPE_QUERYTOGRAPH = 2;
	
	public int type;
	public int status;
	public String name;
	public String description;
	public String errorDescription;

	public Map<String, Double> progressPercentages;
	
}
