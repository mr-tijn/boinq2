package com.genohm.boinq.web.rest.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.QueryDefinition;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QueryDefinitionDTO {
	
	public static QueryDefinitionDTO create(QueryDefinition definition) {
		List<QueryBridgeDTO> bridges = definition.getQueryBridges().stream().map(bridge -> QueryBridgeDTO.create(bridge)).collect(Collectors.toList());
		List<QueryGraphDTO> graphs = definition.getQueryGraphs().stream().map(graph -> QueryGraphDTO.create(graph)).collect(Collectors.toList());
		return create(definition.getId(), definition.getStatus(), definition.getName(), definition.getDescription(), definition.getSpecies(), definition.getAssembly(), definition.getOwner().getLogin(), definition.getResultAsTable(), bridges, graphs, definition.getTargetGraph(), definition.getTargetFile(), definition.getSparqlQuery());
	}
	
	@JsonCreator public static QueryDefinitionDTO create(
			@JsonProperty("id")  			Long id,
			@JsonProperty("status")			Integer status,
			@JsonProperty("name")			String name,
			@JsonProperty("description")	String description,
			@JsonProperty("species")		String species,
			@JsonProperty("assembly")		String assembly,
			@JsonProperty("owner")			String owner,
			@JsonProperty("resultAsTable")	Boolean resultAsTable,
			@JsonProperty("queryBridges")	List<QueryBridgeDTO> queryBridges,
			@JsonProperty("queryGraphs")	List<QueryGraphDTO> queryGraphs,
			@JsonProperty("targetGraph")	String targetGraph,
			@JsonProperty("targetFile")		String targetFile,
			@JsonProperty("sparqlQuery") 	String sparqlQuery) {
		return new AutoValue_QueryDefinitionDTO(id, status, name, description, species, assembly, owner, resultAsTable, queryBridges, queryGraphs, targetGraph, targetFile, sparqlQuery);
	}
	
	
	@JsonProperty("id") @Nullable 			public abstract Long id();
	@JsonProperty("status") 				public abstract Integer status();
	@JsonProperty("name") @Nullable 		public abstract String name();
	@JsonProperty("description") @Nullable 	public abstract String description();
	@JsonProperty("species") @Nullable 		public abstract String species();
	@JsonProperty("assembly") @Nullable 	public abstract String assembly();
	@JsonProperty("owner") @Nullable		public abstract String owner();
	@JsonProperty("resultAsTable")	public abstract Boolean resultAsTable();
	@JsonProperty("queryBridges")	public abstract List<QueryBridgeDTO> queryBridges();
	@JsonProperty("queryGraphs")	public abstract List<QueryGraphDTO> queryGraphs();
	@JsonProperty("targetGraph") @Nullable	public abstract String targetGraph();
	@JsonProperty("targetFile")	@Nullable	public abstract String targetFile();
	@JsonProperty("sparqlQuery") @Nullable	public abstract String sparqlQuery();
	
}
