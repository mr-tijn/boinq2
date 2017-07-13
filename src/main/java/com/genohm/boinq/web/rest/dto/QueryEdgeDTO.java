package com.genohm.boinq.web.rest.dto;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.QueryEdge;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QueryEdgeDTO {

	public static QueryEdgeDTO create(QueryEdge edge) {
		return create(edge.getId(), edge.getTemplate().getId(), edge.getFrom().getIdx(), edge.getTo().getIdx(), edge.getRetrieve());
	}
	
	@JsonCreator public static QueryEdgeDTO create(
			@JsonProperty("id") Long id,
			@JsonProperty("template") Long templateId,
			@JsonProperty("from") Integer fromIdx,
			@JsonProperty("to") Integer toIdx,
			@JsonProperty("retrieve") Boolean retrieve) {
		return new AutoValue_QueryEdgeDTO(id, templateId, fromIdx, toIdx, retrieve);
		
	}

	
	@JsonProperty("id") @Nullable	public abstract Long id();
	@JsonProperty("template") 		public abstract Long templateId();
	@JsonProperty("from") 			public abstract Integer fromIdx();
	@JsonProperty("to")				public abstract Integer toIdx();
	@JsonProperty("retrieve")		public abstract Boolean retrieve();
	
}
