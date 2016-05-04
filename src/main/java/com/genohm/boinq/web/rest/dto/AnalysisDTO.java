package com.genohm.boinq.web.rest.dto;

import java.util.Map;

public class AnalysisDTO {
	public static int TYPE_FEATURESELECTION = 0;
	
	public int type;
	public int status;
	public String name;
	public String description;
	public String errorDescription;

	public Map<String, Double> progressPercentages;
	
}
