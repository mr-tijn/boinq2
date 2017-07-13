package com.genohm.boinq.web.rest.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.QueryGraph;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QueryGraphDTO {

	public static QueryGraphDTO create(QueryGraph query) {
		List<QueryEdgeDTO> queryEdges = query.getQueryEdges().stream().map(edge -> QueryEdgeDTO.create(edge)).collect(Collectors.toList());
		List<QueryNodeDTO> queryNodes = query.getQueryEdges().stream().map(edge -> edge.getFrom()).map(node -> QueryNodeDTO.create(node)).collect(Collectors.toList());
		queryNodes.addAll(query.getQueryEdges().stream().map(edge -> edge.getTo()).map(node -> QueryNodeDTO.create(node)).collect(Collectors.toList()));
		return create(query.getId(), query.getIdx(), query.getX(), query.getY(), query.getName(), query.getTemplate().getId(), queryEdges, queryNodes);
	}
	
	@JsonCreator public static QueryGraphDTO create(
			@JsonProperty("id") Long id,
			@JsonProperty("idx") Integer idx,
			@JsonProperty("x") Integer x,
			@JsonProperty("y") Integer y,
			@JsonProperty("name") String name,
			@JsonProperty("template") Long graphTemplateId,
			@JsonProperty("queryEdges") List<QueryEdgeDTO> queryEdges,
			@JsonProperty("queryNodes") List<QueryNodeDTO> queryNodes) {
		return new AutoValue_QueryGraphDTO(id, idx, x, y, name, graphTemplateId, queryEdges, queryNodes);
	}
	
    @JsonProperty("id") @Nullable	public abstract Long id();
    @JsonProperty("idx")			public abstract Integer idx();
    @JsonProperty("x") @Nullable	public abstract Integer x();
    @JsonProperty("y") @Nullable	public abstract Integer y();
    @JsonProperty("name") @Nullable	public abstract String name();
    @JsonProperty("template") 		public abstract Long graphTemplateId();
    @JsonProperty("queryEdges")		public abstract List<QueryEdgeDTO> queryEdges();
    @JsonProperty("queryNodes")		public abstract List<QueryNodeDTO> queryNodes();
	
}
