package org.boinq.web.rest.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.boinq.domain.query.QueryGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.boinq.web.rest.dto.AutoValue_QueryGraphDTO;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QueryGraphDTO {

	public static QueryGraphDTO create(QueryGraph query) {
		List<QueryEdgeDTO> queryEdges = new LinkedList<>(query.getQueryEdges().stream().map(edge -> QueryEdgeDTO.create(edge)).collect(Collectors.toSet()));
		Set<QueryNodeDTO> queryNodeSet = query.getQueryEdges().stream().map(edge -> edge.getFrom()).map(node -> QueryNodeDTO.create(node)).collect(Collectors.toSet());
		queryNodeSet.addAll(query.getQueryEdges().stream().map(edge -> edge.getTo()).map(node -> QueryNodeDTO.create(node)).collect(Collectors.toSet()));
		List<QueryNodeDTO> queryNodes = new LinkedList<>(queryNodeSet);
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
