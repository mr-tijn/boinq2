package com.genohm.boinq.web.rest.dto;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genohm.boinq.domain.query.NodeTemplate;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class NodeTemplateDTO {

	public static NodeTemplateDTO create(NodeTemplate template) {
		return new AutoValue_NodeTemplateDTO(
				template.getId(),
				template.getName(), 
				template.getDescription(), 
				template.getVariablePrefix(), 
				template.getValueSource(), 
				template.getValuesTermList(), 
				template.getValuesEndpoint(), 
				template.getValuesGraph(), 
				template.getValuesRootTerm(), 
				template.getFixedValue(), 
				template.getLiteralXsdType(), 
				template.getFilterable(), 
				template.getColor(), 
				template.getX(), 
				template.getY());
	}
	
	@JsonCreator public static NodeTemplateDTO create(
			@JsonProperty("id") Long id,
			@JsonProperty("name") String name,
			@JsonProperty("description") String description,
			@JsonProperty("variablePrefix") String variablePrefix,
			@JsonProperty("valueSource") int valueSource,
			@JsonProperty("valuesTermList") List<String> valuesTermList,
			@JsonProperty("valuesEndpoint") String valuesEndpoint,
			@JsonProperty("valuesGraph") String valuesGraph,
			@JsonProperty("valuesRootTerm") String valuesRootTerm,
			@JsonProperty("fixedValue") String fixedValue,
			@JsonProperty("literalXsdType") String literalXsdType,
			@JsonProperty("filterable") Boolean filterable,
			@JsonProperty("color") String color,
			@JsonProperty("x") int x,
			@JsonProperty("y") int y) {
		return new AutoValue_NodeTemplateDTO(id, name, description, variablePrefix, valueSource, valuesTermList, valuesEndpoint, valuesGraph, valuesRootTerm, fixedValue, literalXsdType, filterable, color, x, y);
	}
	

	@JsonProperty("id") @Nullable 	public abstract Long id();
	@JsonProperty("name") @Nullable public abstract String name();
	@JsonProperty("description") @Nullable public abstract String description();
	@JsonProperty("variablePrefix") @Nullable public abstract String variablePrefix();
	@JsonProperty("valueSource")		  public abstract Integer valueSource();
	@JsonProperty("valuesTermList") @Nullable public abstract List<String> valuesTermList();
	@JsonProperty("valuesEndpoint") @Nullable public abstract String valuesEndpoint();
	@JsonProperty("valuesGraph") @Nullable public abstract String valuesGraph();
	@JsonProperty("valuesRootTerm") @Nullable public abstract String valuesRootTerm();
	@JsonProperty("fixedValue") @Nullable public abstract String fixedValue();
	@JsonProperty("literalXsdType")@Nullable public abstract String literalXsdType();
	@JsonProperty("filterable")public abstract Boolean filterable();
	@JsonProperty("color")public abstract String color();
	@JsonProperty("x") public abstract Integer x();
	@JsonProperty("y")		  public abstract Integer y();
}
