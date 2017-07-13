package com.genohm.boinq.web.rest.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.QueryNode;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QueryNodeDTO {
	
	public static QueryNodeDTO create(QueryNode node) {
		List<NodeFilterDTO> filters = node.getNodeFilters().stream().map(filter -> NodeFilterDTO.create(filter)).collect(Collectors.toList());
		return create(node.getId(), node.getTemplate().getId(), filters, node.getEntityValues(), node.getIdx());
	}

	@JsonCreator public static QueryNodeDTO create(
		    @JsonProperty("id")	Long id,
			@JsonProperty("template") Long templateId,
			@JsonProperty("nodeFilters") List<NodeFilterDTO> nodeFilters,
			@JsonProperty("entityValues") List<String> entityValues,
			@JsonProperty("idx") Integer idx
			) {
		 	return new AutoValue_QueryNodeDTO(id, templateId, nodeFilters, entityValues, idx);
		 }
	
    @JsonProperty("id")	@Nullable	public abstract Long id();
	@JsonProperty("template")		public abstract Long templateId();
	@JsonProperty("nodeFilters")	public abstract List<NodeFilterDTO> nodeFilters();
	@JsonProperty("entityValues")	public abstract List<String> entityValues();
	@JsonProperty("idx")			public abstract Integer idx();

	
}
