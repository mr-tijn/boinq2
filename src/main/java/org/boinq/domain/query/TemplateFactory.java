package org.boinq.domain.query;

import org.apache.jena.graph.Node;
import org.apache.jena.vocabulary.RDF;

import com.google.common.base.CaseFormat;

public class TemplateFactory {
	
	
	public static class Increment {
		// simple incrementer to be able to share state of counters
		// not really needed anymore
		private Integer count;
		public Increment(Integer init) {
			this.count = init; 
		}
		public Integer next() {
			return count++;
		}
	}
	
	public static class Position {
		// simple helper for positioning nodes
		public int x;
		public int y;
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		private int cartesianX(double r, double theta) {
			return (int) Math.round(r*Math.cos(Math.PI*theta/180.));
		}
		
		private int cartesianY(double r, double theta) {
			return (int) Math.round(r*Math.sin(Math.PI*theta/180.));
		}
		public void up(int offset) { y -= offset; }
		public void down(int offset) { y += offset; }
		public void left(int offset) { x -= offset; }
		public void right(int offset) { x += offset; }
		public void angle(int offset, double theta) {
			x += cartesianX(offset, theta);
			y += cartesianY(offset, theta);
		}
		public void move(int x, int y) { this.x = x; this.y = y; }
		public void to(Position to) { this.x = to.x; this.y = to.y; }
		public Position copy() { return new Position(this.x, this.y); }
	}

	
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
	
	public static NodeTemplate attributeNode(String name, String attributeType, String valueType, Position pos, Increment inc) {
		NodeTemplate attributeNode = attributeNode(name, attributeType, valueType);
		attributeNode.setX(pos.x);
		attributeNode.setY(pos.y);
		attributeNode.setIdx(inc.next());
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

	public static NodeTemplate typedEntityNode(String name, String type, Boolean filterable, Position pos, Increment inc) {
		NodeTemplate entityNode = typedEntityNode(name, type, filterable);
		entityNode.setIdx(inc.next());
		entityNode.setX(pos.x);
		entityNode.setY(pos.y);
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
	
	public static NodeTemplate entityNode(String name, String variablePrefix, Boolean filterable, Position pos, Increment inc) {
		NodeTemplate entityNode = entityNode(name, filterable);
		entityNode.setIdx(inc.next());
		entityNode.setX(pos.x);
		entityNode.setY(pos.y);
		return entityNode;
	}
	
	public static NodeTemplate locationNode(String assembly, Position pos, Increment inc) {
		NodeTemplate locationNode = new NodeTemplate();
		locationNode.setNodeType(QueryNode.NODETYPE_FALDOLOCATION);
		locationNode.setName("Faldo Location");
		locationNode.setDescription("Location according the FALDO ontology");
		locationNode.setAssembly(assembly);
		locationNode.setColor("green");
		locationNode.setFilterable(true);
		locationNode.setIdx(inc.next());
		locationNode.setX(pos.x);
		locationNode.setY(pos.y);
		return locationNode;
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
	
	public static NodeTemplate literalNode(String name, String type, Position pos, Increment inc) {
		NodeTemplate literalNode = literalNode(name, type);
		literalNode.setIdx(inc.next());
		literalNode.setX(pos.x);
		literalNode.setY(pos.y);
		return literalNode;
	}

	public static NodeTemplate literalNode(String name, Node type) {
		return literalNode(name, type.toString());
	}
	
	public static NodeTemplate literalNode(String name, Node type, Position pos, Increment inc) {
		NodeTemplate literalNode = literalNode(name, type);
		literalNode.setIdx(inc.next());
		literalNode.setX(pos.x);
		literalNode.setY(pos.y);
		return literalNode;
	}
	
	public static EdgeTemplate typeEdgeTemplate(NodeTemplate template, Node type) {
		return typeEdgeTemplate(template, type.toString());
	}
	
	public static EdgeTemplate typeEdgeTemplate(NodeTemplate template, String type) {
		NodeTemplate typeNodeTemplate = entityNode(type, "type", false);
		return new EdgeTemplate(template, typeNodeTemplate, RDF.type.toString());
	}
	
	public static EdgeTemplate link(NodeTemplate from, NodeTemplate to, String edge) {
		return new EdgeTemplate(from, to, edge);
	}
	
	public static EdgeTemplate link(NodeTemplate from, NodeTemplate to, String edge, String label) {
		EdgeTemplate result = link(from, to, edge);
		result.setLabel(label);
		return result;
	}

}
