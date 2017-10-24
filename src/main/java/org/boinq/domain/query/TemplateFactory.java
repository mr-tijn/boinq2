package org.boinq.domain.query;

import org.apache.jena.graph.Node;
import org.apache.jena.vocabulary.RDF;

import com.google.common.base.CaseFormat;

public class TemplateFactory {
	
	private static String fmt(String name) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name.toLowerCase().replaceAll(" ", "_"));
	}
	
	public static NodeTemplate entityNode(String name, Boolean filterable) {
		NodeTemplate entityNode = new NodeTemplate();
		entityNode.setName(name);
		entityNode.setVariablePrefix(fmt(name));
		entityNode.setFilterable(filterable);
		entityNode.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
		entityNode.setColor("blue");
		return entityNode;
	}
	
	public static NodeTemplate attributeNode(String name, String attributeType, String valueType) {
		NodeTemplate attributeNode = new NodeTemplate();
		attributeNode.setName(name);
		attributeNode.setVariablePrefix(fmt(name));
		attributeNode.setFilterable(true);
		attributeNode.setNodeType(QueryNode.NODETYPE_ATTRIBUTE);
		attributeNode.setFixedType(attributeType);
		attributeNode.setLiteralXsdType(valueType);
		attributeNode.setColor("blue");
		return attributeNode;
	}

	public static NodeTemplate typedEntityNode(String name, String type, Boolean filterable) {
		NodeTemplate entityNode = new NodeTemplate();
		entityNode.setName(name);
		entityNode.setVariablePrefix(fmt(name));
		entityNode.setFilterable(filterable);
		entityNode.setNodeType(QueryNode.NODETYPE_TYPEDENTITY);
		entityNode.setFixedType(type);
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
	
	public static NodeTemplate locationNode(String assembly) {
		NodeTemplate locationNode = new NodeTemplate();
		locationNode.setNodeType(QueryNode.NODETYPE_FALDOLOCATION);
		locationNode.setName("Faldo Location");
		locationNode.setDescription("Location according the FALDO ontology");
		locationNode.setAssembly(assembly);
		locationNode.setColor("green");
		locationNode.setFilterable(true);
		return locationNode;
	}
	
	public static NodeTemplate literalNode(String name, Node type) {
		return literalNode(name, type.toString());
	}
	
	public static NodeTemplate literalNode(String name, String type) {
		NodeTemplate literalNode = new NodeTemplate();
		literalNode.setName(name);
		literalNode.setNodeType(QueryNode.NODETYPE_GENERICLITERAL);
		literalNode.setColor("yellow");
		literalNode.setLiteralXsdType(type);
		literalNode.setFilterable(true);
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
