package com.genohm.boinq.web.rest.dto;

import java.util.Set;

public class FeatureSelectDTO {
	
	public static final String FALDO_SELECT_TYPE = "FaldoSelect";
	
	public String type = FALDO_SELECT_TYPE;
	public int idx; //local identifier
	public int trackId;
	public Boolean retrieve;
	public Set<CriteriaDTO> criteria;
	public int viewX;
	public int viewY;
}
