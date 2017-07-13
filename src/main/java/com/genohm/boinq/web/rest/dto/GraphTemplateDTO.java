package com.genohm.boinq.web.rest.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.domain.query.NodeTemplate;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GraphTemplateDTO {

	public static GraphTemplateDTO create(GraphTemplate template) {
		List<EdgeTemplateDTO> edgeTemplates = template.getEdgeTemplates().stream().map(edgeTemplate -> EdgeTemplateDTO.create(edgeTemplate)).collect(Collectors.toList());
		Set<NodeTemplate> nodes = new HashSet<>();
		nodes.addAll(template.getEdgeTemplates().stream().map(edge -> edge.getFrom()).collect(Collectors.toSet()));
		nodes.addAll(template.getEdgeTemplates().stream().map(edge -> edge.getTo()).collect(Collectors.toSet()));
		List<NodeTemplateDTO> nodeTemplates = nodes.stream().map(node -> NodeTemplateDTO.create(node)).collect(Collectors.toList());
		return new AutoValue_GraphTemplateDTO(template.getId(), template.getType(), template.getName(), template.getEndpointUrl(), template.getGraphIri(), edgeTemplates, nodeTemplates);
	}
	
	@JsonCreator public static GraphTemplateDTO create(
			@JsonProperty("id") Long id, 
			@JsonProperty("type") int type, 
			@JsonProperty("name") String name,
			@JsonProperty("endpointUrl") String endpointUrl,
			@JsonProperty("graphIri") String graphIri,
			@JsonProperty("edgeTemplates") List<EdgeTemplateDTO> edgeTemplates,
			@JsonProperty("nodeTemplates") List<NodeTemplateDTO> nodeTemplates) {
		return new AutoValue_GraphTemplateDTO(id, type, name, endpointUrl, graphIri, edgeTemplates, nodeTemplates);
	}
	
	@JsonProperty("id") @Nullable 			public abstract Long id();
	@JsonProperty("type") 		  			public abstract Integer type();
	@JsonProperty("name") @Nullable 		public abstract String name();
	@JsonProperty("endpointUrl") @Nullable 	public abstract String endpointUrl();
	@JsonProperty("graphIri") @Nullable 	public abstract String graphIri();
	@JsonProperty("edgeTemplates")	  		public abstract List<EdgeTemplateDTO> edgeTemplates();
	@JsonProperty("nodeTemplates")			public abstract List<NodeTemplateDTO> nodeTemplates();
}
