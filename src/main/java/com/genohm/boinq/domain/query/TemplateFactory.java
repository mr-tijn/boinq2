package com.genohm.boinq.domain.query;

import org.apache.jena.vocabulary.RDF;

import com.github.jsonldjava.core.RDFDataset.Node;
import com.google.common.base.CaseFormat;

public class TemplateFactory {
	
	public static NodeTemplate entityNode(String name, Boolean filterable) {
		NodeTemplate entityNode = new NodeTemplate();
		entityNode.setName(name);
		entityNode.setVariablePrefix(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name.toLowerCase().replaceAll(" ", "_")));
		entityNode.setFilterable(filterable);
		entityNode.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
		entityNode.setColor("blue");
		return entityNode;
	}

	public static NodeTemplate entityNode(String name, String variablePrefix, Boolean filterable) {
		NodeTemplate entityNode = new NodeTemplate();
		entityNode.setName(name);
		entityNode.setVariablePrefix(variablePrefix);
		entityNode.setFilterable(filterable);
		entityNode.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
		entityNode.setColor("blue");
		return entityNode;
	}
	
	public static NodeTemplate literalNode(String name, Node type) {
		return literalNode(name, type.toString());
	}
	
	public static NodeTemplate literalNode(String name, String type) {
		NodeTemplate literalNode = new NodeTemplate();
		literalNode.setName(name);
		literalNode.setNodeType(QueryNode.NODETYPE_GENERICLITERAL);
		literalNode.setColor("green");
		literalNode.setLiteralXsdType(type);
		return literalNode;
	}
	
	public static EdgeTemplate typeEdgeTemplate(NodeTemplate template, Node type) {
		return typeEdgeTemplate(template, type.toString());
	}
	
	public static EdgeTemplate typeEdgeTemplate(NodeTemplate template, String type) {
		NodeTemplate typeNodeTemplate = entityNode(type, "type", false);
		return new EdgeTemplate(template, typeNodeTemplate, RDF.type.toString());
	}

}