package com.genohm.boinq.web.rest.dto;

import java.util.Set;

public class FeatureQueryDTO {
	public Long id;
	public Set<FeatureJoinDTO> joins;
	public Set<FeatureSelectDTO> selects;
	public String ownerId;
	public String name;
	
	public FeatureQueryDTO() {}
	
}
