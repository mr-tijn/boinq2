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
				template.getFrom().getIdx(), 
				template.getTo().getIdx(), 
				template.getTerm(),
				template.getLabel());
	}
	
	@JsonCreator public static EdgeTemplateDTO create(
			@JsonProperty("id") Long id,
			@JsonProperty("from") Integer fromIdx,
			@JsonProperty("to") Integer toIdx,
			@JsonProperty("term") String term,
			@JsonProperty("label") String label) {
		return new AutoValue_EdgeTemplateDTO(id, fromIdx, toIdx, term, label);
	}
	
	@JsonProperty("id") @Nullable public abstract Long id();
	@JsonProperty("from") public abstract Integer fromIdx();
	@JsonProperty("to") public abstract Integer toIdx();
	@JsonProperty("term") public abstract String term();
	@JsonProperty("label") @Nullable public abstract String label();
}
