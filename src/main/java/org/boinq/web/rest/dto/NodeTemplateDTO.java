package org.boinq.web.rest.dto;

import java.util.List;

import javax.annotation.Nullable;

import org.boinq.domain.query.NodeTemplate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.boinq.web.rest.dto.AutoValue_NodeTemplateDTO;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class NodeTemplateDTO {

	public static NodeTemplateDTO create(NodeTemplate template) {
		return new AutoValue_NodeTemplateDTO(
				template.getId(),
				template.getName(), 
				template.getNodeType(),
				template.getDescription(), 
				template.getVariablePrefix(), 
				template.getValueSource(), 
				template.getValuesTermList(), 
				template.getValuesEndpoint(), 
				template.getValuesGraph(), 
				template.getValuesRootTerm(), 
				template.getFixedValue(), 
				template.getFixedType(),
				template.getLiteralXsdType(),
				template.getAssembly(),
				template.getFilterable(), 
				template.getColor(), 
				template.getX(), 
				template.getY(),
				template.getIdx());
	}
	
	@JsonCreator public static NodeTemplateDTO create(
			@JsonProperty("id") Long id,
			@JsonProperty("name") String name,
			@JsonProperty("nodeType") Integer nodeType,
			@JsonProperty("description") String description,
			@JsonProperty("variablePrefix") String variablePrefix,
			@JsonProperty("valueSource") int valueSource,
			@JsonProperty("valuesTermList") List<String> valuesTermList,
			@JsonProperty("valuesEndpoint") String valuesEndpoint,
			@JsonProperty("valuesGraph") String valuesGraph,
			@JsonProperty("valuesRootTerm") String valuesRootTerm,
			@JsonProperty("fixedValue") String fixedValue,
			@JsonProperty("fixedType") String fixedType,
			@JsonProperty("literalXsdType") String literalXsdType,
			@JsonProperty("assembly") String assembly,
			@JsonProperty("filterable") Boolean filterable,
			@JsonProperty("color") String color,
			@JsonProperty("x") int x,
			@JsonProperty("y") int y,
			@JsonProperty("idx") Integer idx) {
		return new AutoValue_NodeTemplateDTO(id, name, nodeType, description, variablePrefix, valueSource, valuesTermList, valuesEndpoint, valuesGraph, valuesRootTerm, fixedValue, fixedType, literalXsdType, assembly, filterable, color, x, y, idx);
	}
	

	@JsonProperty("id") @Nullable 	public abstract Long id();
	@JsonProperty("name") @Nullable public abstract String name();
	@JsonProperty("nodeType") 			public abstract Integer nodeType();
	@JsonProperty("description") @Nullable public abstract String description();
	@JsonProperty("variablePrefix") @Nullable public abstract String variablePrefix();
	@JsonProperty("valueSource")		  public abstract Integer valueSource();
	@JsonProperty("valuesTermList") @Nullable public abstract List<String> valuesTermList();
	@JsonProperty("valuesEndpoint") @Nullable public abstract String valuesEndpoint();
	@JsonProperty("valuesGraph") @Nullable public abstract String valuesGraph();
	@JsonProperty("valuesRootTerm") @Nullable public abstract String valuesRootTerm();
	@JsonProperty("fixedValue") @Nullable public abstract String fixedValue();
	@JsonProperty("fixedType") @Nullable public abstract String fixedType();
	@JsonProperty("literalXsdType")@Nullable public abstract String literalXsdType();
	@JsonProperty("assembly") @Nullable public abstract String assembly();
	@JsonProperty("filterable")public abstract Boolean filterable();
	@JsonProperty("color")public abstract String color();
	@JsonProperty("x") public abstract Integer x();
	@JsonProperty("y")		  public abstract Integer y();
	@JsonProperty("idx") public abstract Integer idx();
}
