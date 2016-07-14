package com.genohm.boinq.web.rest.dto;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.EdgeTemplate;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class EdgeTemplateDTO {

	public static EdgeTemplateDTO create(EdgeTemplate template) {
		return new AutoValue_EdgeTemplateDTO(
				template.getId(), 
				NodeTemplateDTO.create(template.getFrom()), 
				NodeTemplateDTO.create(template.getTo()), 
				template.getTerm());
	}
	
	@JsonCreator public static EdgeTemplateDTO create(
			@JsonProperty("id") Long id,
			@JsonProperty("from") NodeTemplateDTO from,
			@JsonProperty("to") NodeTemplateDTO to,
			@JsonProperty("term") String term) {
		return new AutoValue_EdgeTemplateDTO(id, from, to, term);
	}
	
	@JsonProperty("id") @Nullable public abstract Long id();
	@JsonProperty("from") public abstract NodeTemplateDTO from();
	@JsonProperty("to") public abstract NodeTemplateDTO to();
	@JsonProperty("term") public abstract String term();
}
