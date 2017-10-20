package com.genohm.boinq.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.E_GreaterThan;
import org.apache.jena.sparql.expr.E_GreaterThanOrEqual;
import org.apache.jena.sparql.expr.E_IRI;
import org.apache.jena.sparql.expr.E_LessThan;
import org.apache.jena.sparql.expr.E_LessThanOrEqual;
import org.apache.jena.sparql.expr.E_LogicalAnd;
import org.apache.jena.sparql.expr.E_LogicalNot;
import org.apache.jena.sparql.expr.E_NotEquals;
import org.apache.jena.sparql.expr.E_OneOf;
import org.apache.jena.sparql.expr.E_SameTerm;
import org.apache.jena.sparql.expr.E_Str;
import org.apache.jena.sparql.expr.E_StrAfter;
import org.apache.jena.sparql.expr.E_StrContains;
import org.apache.jena.sparql.expr.E_StrStartsWith;
import org.apache.jena.sparql.expr.E_StrUpperCase;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;
import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.query.EdgeTemplate;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.domain.query.NodeFilter;
import com.genohm.boinq.domain.query.NodeTemplate;
import com.genohm.boinq.domain.query.QueryBridge;
import com.genohm.boinq.domain.query.QueryDefinition;
import com.genohm.boinq.domain.query.QueryEdge;
import com.genohm.boinq.domain.query.QueryGraph;
import com.genohm.boinq.domain.query.QueryNode;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;

@Service
public class GenerateQueryService {
	
	private static final Map<String, Long> idCounters = new HashMap<String, Long>();
	
	//TODO: ignore selected edges that are not selected for retrieve and do not contain filtered nodes or bridges
	//TODO: join queries on same graph
	
	@Value(value="{querybuilder.prefix}")
	private String prefix;
	@Value(value="")
	private String localSparqlUpdateEndpoint;
	
	public String generateQuery(QueryDefinition query) {
		Map<QueryNode, String> variableNames = 	generateVariableNames(query);
		Map<QueryGraph, ElementTriplesBlock> graphTriples = buildGraphTriples(variableNames, query);
		Map<QueryGraph, Map<QueryNode,List<ElementFilter>>> graphFilters = buildGraphFilters(variableNames, query);
		List<ElementFilter> globalFilters = generateBridgeFilters(variableNames, query);
		Element mainSelect = buildSelect(query, graphTriples, graphFilters, globalFilters);
		if (query.getResultAsTable()) {
			Query selectQuery = new Query();
			selectQuery.setQuerySelectType();
			selectQuery.setQueryPattern(mainSelect);
			for (String varName: getReturnVariableNames(query, variableNames)) {
				selectQuery.addResultVar(NodeFactory.createVariable(varName));
			}
			return selectQuery.toString(Syntax.syntaxSPARQL_11);
		} else {
			// make new graph
			UpdateModify updateQuery = new UpdateModify();
			Set<Triple> insertTriples = buildInsertTriples(variableNames, query);
			updateQuery.setElement(mainSelect);
			for (Triple triple: insertTriples) {
				updateQuery.getInsertAcc().addQuad(new Quad(NodeFactory.createURI(query.getTargetGraph()), triple));
			}
			buildGraphTemplate(variableNames, query);
			
			// TODO: add prefix map
			return updateQuery.toString();
		}
	}
	
	
	private Element buildSelect(QueryDefinition qd, Map<QueryGraph, ElementTriplesBlock> triplesByGraph, Map<QueryGraph, Map<QueryNode, List<ElementFilter>>> filtersByNodeByGraph, List<ElementFilter> globalFilters) {
		ElementGroup mainSelect = new ElementGroup();
		// new
		Map<GraphTemplate, List<QueryGraph>> graphsByTemplate = new HashMap<>();
		for (QueryGraph graph: qd.getQueryGraphs()) {
			if (graphsByTemplate.containsKey(graph.getTemplate())) {
				graphsByTemplate.get(graph.getTemplate()).add(graph);
			} else {
				List<QueryGraph> newList = new LinkedList<>();
				newList.add(graph);
				graphsByTemplate.put(graph.getTemplate(), newList);
			}
		}
		for (GraphTemplate gt: graphsByTemplate.keySet()) {
			ElementGroup element = new ElementGroup();
			for (QueryGraph graph: graphsByTemplate.get(gt)) {
				element.addElement(triplesByGraph.get(graph));
				Map<QueryNode, List<ElementFilter>> filtersByNode = filtersByNodeByGraph.get(graph);
				for (QueryNode node: filtersByNode.keySet()) {
					for (ElementFilter filter: filtersByNode.get(node)) {
						element.addElement(filter);
					}
				}
			}
			ElementNamedGraph graphElement = new ElementNamedGraph(NodeFactory.createURI(gt.getGraphIri()), element);
			if (gt.getType() == GraphTemplate.GRAPH_TYPE_REMOTE) {
				mainSelect.addElement(new ElementService(gt.getEndpointUrl(), graphElement));
			} else {
				mainSelect.addElement(graphElement);
			}
		}
		// end new
		// old
//		for (QueryGraph graph: qd.getQueryGraphs()) {
//			ElementGroup element = new ElementGroup();
//			element.addElement(triplesByGraph.get(graph));
//			Map<QueryNode, List<ElementFilter>> filtersByNode = filtersByNodeByGraph.get(graph);
//			for (QueryNode node: filtersByNode.keySet()) {
//				for (ElementFilter filter: filtersByNode.get(node)) {
//					element.addElement(filter);
//				}
//			}
//			ElementNamedGraph graphElement = new ElementNamedGraph(NodeFactory.createURI(graph.getTemplate().getGraphIri()), element);
//			if (graph.getTemplate().getType() == GraphTemplate.GRAPH_TYPE_REMOTE) {
//				mainSelect.addElement(new ElementService(graph.getTemplate().getEndpointUrl(), graphElement));
//			} else {
//				mainSelect.addElement(graphElement);
//			}
//		}
		// end old
		for (ElementFilter filter: globalFilters) {
			mainSelect.addElement(filter);
		}
		return mainSelect;
	}
	
	private Set<String> getReturnVariableNames(QueryDefinition query, Map<QueryNode, String> variableNames) {
		Set<String> result = new HashSet<>();
		for (QueryGraph graph: query.getQueryGraphs()) {
			for (QueryEdge edge: graph.getQueryEdges()) {
				result.add(variableNames.get(edge.getFrom()));
				result.add(variableNames.get(edge.getTo()));
			}
		}
		return result;
	}

	private Node localUri(String postfix) {
		return NodeFactory.createURI(prefix + postfix);
	}
	
	private Map<QueryNode, String> generateVariableNames(QueryDefinition query) {
		Map<QueryNode, String> variableNames = new HashMap<>();	
		// find all interconnected entity bridges and use one name for all nodes in each set
		Set<Set<QueryBridge>> clusters = query.findBridgeHubs();
		for (Set<QueryBridge> cluster: clusters) {
			String variableName = nextId("xref");
			for (QueryBridge bridge: cluster) {
				if (QueryBridge.BRIDGE_TYPE_LITERAL_TO_LITERAL != bridge.getType() &&
					QueryBridge.BRIDGE_TYPE_LOCATION != bridge.getType()) {
					variableNames.put(bridge.getFromNode(), variableName);
					variableNames.put(bridge.getToNode(), variableName);
				}
			}
		}
		// name all other unnamed nodes 
		for (QueryGraph graph: query.getQueryGraphs()) {
			String graphName = graph.getName().replace(" ", "_");
			for (QueryEdge edge: graph.getQueryEdges()) {
				//TODO: handle valued entities (typical for type): should not have a variable but a constant
				if (!variableNames.containsKey(edge.getFrom()) && NodeTemplate.SOURCE_FIXED != edge.getFrom().getTemplate().getValueSource()) {
					String varPrefix = edge.getFrom().getTemplate().getVariablePrefix();
					if (varPrefix == null) varPrefix = graphName + "_entity";
					variableNames.put(edge.getFrom(), nextId(varPrefix));
				}
				if (!variableNames.containsKey(edge.getTo()) && NodeTemplate.SOURCE_FIXED != edge.getTo().getTemplate().getValueSource()) {
					String varPrefix = edge.getTo().getTemplate().getVariablePrefix();
					if (varPrefix == null) varPrefix = graphName + "_entity";
					variableNames.put(edge.getTo(), nextId(varPrefix));
				}
			}
		}
		return variableNames;
	}
	
	private Map<QueryGraph, ElementTriplesBlock> buildGraphTriples(Map<QueryNode, String> variableNames, QueryDefinition qd) {
		Map<QueryGraph, ElementTriplesBlock> resultMap = new HashMap<>();
		for (QueryGraph graph: qd.getQueryGraphs()) {
			ElementTriplesBlock triples = new ElementTriplesBlock();
			for (QueryEdge edge: graph.getQueryEdges()) {
				//TODO: handle valued entities (typical for type): should not have a variable but a constant
				Node fromNode;
				Node toNode;
				if (NodeTemplate.SOURCE_FIXED == edge.getFrom().getTemplate().getValueSource()) {
					fromNode = NodeFactory.createURI(edge.getFrom().getTemplate().getFixedValue());
				} else {
					fromNode = NodeFactory.createVariable(variableNames.get(edge.getFrom()));
				}
				if (NodeTemplate.SOURCE_FIXED == edge.getTo().getTemplate().getValueSource()) {
					toNode = NodeFactory.createURI(edge.getTo().getTemplate().getFixedValue());
				} else {
					toNode = NodeFactory.createVariable(variableNames.get(edge.getTo()));
				}
				triples.addTriple(new Triple(fromNode, NodeFactory.createURI(edge.getTemplate().getTerm()), toNode));
				if (QueryNode.NODETYPE_FALDOLOCATION == edge.getFrom().getNodeType()) {
					// allow only one faldo filter in editor
					Optional<NodeFilter> faldoFilter = edge.getFrom().getNodeFilters().stream().filter(nodeFilter -> NodeFilter.FILTER_TYPE_FALDOLOCATION == nodeFilter.getType()).findFirst();
					for (Triple triple: faldoTriples(variableNames.get(edge.getFrom()), faldoFilter)) {
						triples.addTriple(triple);
					}
				}
				if (QueryNode.NODETYPE_FALDOLOCATION == edge.getTo().getNodeType()) {
					Optional<NodeFilter> faldoFilter = edge.getTo().getNodeFilters().stream().filter(nodeFilter -> NodeFilter.FILTER_TYPE_FALDOLOCATION == nodeFilter.getType()).findFirst();
					for (Triple triple: faldoTriples(variableNames.get(edge.getTo()), faldoFilter)) {
						triples.addTriple(triple);
					}
				}
			}
			resultMap.put(graph, triples);
		}
		return resultMap;
	}
	
	private Node beginVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_Begin");}
	private Node endVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_End");}
	private Node beginPosVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_BeginPos");}
	private Node endPosVar(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_EndPos");}
	private Node referenceVar(String featureVarName) {return NodeFactory.createVariable(featureVarName+"_Reference");}
	private Node positionType(String featureVarName) {return NodeFactory.createVariable(featureVarName + "_PositionType");}
	
	private List<Triple> faldoTriples(String locationVariableName, Optional<NodeFilter> maybeFilter) {
		List<Triple> triples = new LinkedList<Triple>();
		Node featureLocation = NodeFactory.createVariable(locationVariableName);
		Node featureBegin = beginVar(locationVariableName);
		Node featureBeginPos = beginPosVar(locationVariableName);
		Node featureReference = referenceVar(locationVariableName);
		Node featureReferenceName = NodeFactory.createVariable(locationVariableName+ "_ReferenceName");
		Node featurePositionType = positionType(locationVariableName);
		triples.add(new Triple(featureLocation, FaldoVocab.begin.asNode(), featureBegin));
		triples.add(new Triple(featureBegin, FaldoVocab.position.asNode(), featureBeginPos));
		triples.add(new Triple(featureBegin, FaldoVocab.reference.asNode(), featureReference));
		triples.add(new Triple(featureReference, RDFS.label.asNode(), featureReferenceName));
		Node featureEnd = endVar(locationVariableName);
		Node featureEndPos = endPosVar(locationVariableName);
		triples.add(new Triple(featureLocation, FaldoVocab.end.asNode(), featureEnd));
		triples.add(new Triple(featureEnd, FaldoVocab.position.asNode(), featureEndPos));
		triples.add(new Triple(featureBegin, RDF.type.asNode(), featurePositionType));
		if (maybeFilter.isPresent()) {
			NodeFilter filter = maybeFilter.get();
			if (null != filter.getStrand()) {
				if (filter.getStrand()) {
					triples.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ForwardStrandPosition.asNode()));
				} else {
					triples.add(new Triple(featureBegin, RDF.type.asNode(), FaldoVocab.ReverseStrandPosition.asNode()));
				}
			}
			if (filter.getContig() != null) {
				triples.add(new Triple(featureBegin, FaldoVocab.reference.asNode(), NodeFactory.createURI(filter.getContig())));
			}
		}
		return triples;
	}
	
	
	private List<ElementFilter> faldoFilters(String locationVariableName, Optional<NodeFilter> maybeFilter) {
		List<ElementFilter> faldoFilters = new LinkedList<>();
		ExprList targetExpressions = new ExprList();
		targetExpressions.add(new E_IRI(new NodeValueNode(FaldoVocab.ForwardStrandPosition.asNode())));
		targetExpressions.add(new E_IRI(new NodeValueNode(FaldoVocab.ReverseStrandPosition.asNode())));
		E_OneOf orientations = new E_OneOf(new E_IRI(new ExprVar(positionType(locationVariableName))), targetExpressions);
		faldoFilters.add(new ElementFilter(orientations));
		if (maybeFilter.isPresent()) {
			NodeFilter filter = maybeFilter.get();
			if (null != filter.getMinInteger()) {
				NodeValue start = filter.getMinValue(XSD.xlong.toString());
				ExprVar endPos = new ExprVar(endPosVar(locationVariableName));
				faldoFilters.add(new ElementFilter(new E_GreaterThanOrEqual(endPos, start)));
			}
			if (null != filter.getMaxInteger()) {
				NodeValue end = filter.getMaxValue(XSD.xlong.toString());
				ExprVar beginPos = new ExprVar(beginPosVar(locationVariableName));
				faldoFilters.add(new ElementFilter(new E_LessThanOrEqual(beginPos, end)));
			}
			// strand and contig are handled by triples
		}
		return faldoFilters;
	}
	
	private Map<QueryGraph, Map<QueryNode,List<ElementFilter>>> buildGraphFilters(Map<QueryNode, String> variableNames, QueryDefinition qd) {
		Map<QueryGraph, Map<QueryNode,List<ElementFilter>>> graphFilters = new HashMap<>();
		for (QueryGraph graph: qd.getQueryGraphs()) {
			Map<QueryNode, List<ElementFilter>> filterMap = new HashMap<>();
			for (QueryEdge edge: graph.getQueryEdges()) {
				QueryNode node = edge.getFrom();
				if (!filterMap.containsKey(node)) {
					filterMap.put(node, generateFilters(node, variableNames.get(node)));
				}
				node = edge.getTo();
				if (!filterMap.containsKey(node)) {
					filterMap.put(node, generateFilters(node, variableNames.get(node)));
				}

			}
			graphFilters.put(graph, filterMap);
		}
		return graphFilters;
	}
	
	private Boolean isString(String xsdType) {
		return xsdType.equals(XSD.xstring);
	}
	private Boolean isInteger(String xsdType) {
		return xsdType.equals(XSD.integer) || xsdType.equals(XSD.xlong) || xsdType.equals(XSD.xshort) || xsdType.equals(XSD.xbyte);
	}
	private Boolean isFloat(String xsdType) {
		return xsdType.equals(XSD.xfloat) || xsdType.equals(XSD.xdouble) || xsdType.equals(XSD.decimal);
	}
	

	private List<ElementFilter> generateFilters(QueryNode node, String variableName) {
		List<ElementFilter> result = new LinkedList<>();
		ExprVar variable = new ExprVar(variableName);
		if (QueryNode.NODETYPE_GENERICENTITY == node.getNodeType()) {
			for (NodeFilter filter: node.getNodeFilters()) {
				if (NodeFilter.FILTER_TYPE_GENERIC_VALUES == filter.getType()) {
					ExprList targetExpressions = new ExprList();
					for (String term: filter.getTermValues()) {
						targetExpressions.add(new NodeValueString(term));
					}
					E_OneOf targetMatch = new E_OneOf(new E_IRI(new ExprVar(variableName)), targetExpressions);
					result.add(new ElementFilter(targetMatch));
				}
			}
		} else if (QueryNode.NODETYPE_GENERICLITERAL == node.getNodeType()) {
			for (NodeFilter filter: node.getNodeFilters()) {
				
				Expr expr = null;
				
						
				// compute type (a bit redundant; need to fix)
				
				if (filter.getExactMatch()) {
					filter.setType(NodeFilter.FILTER_TYPE_GENERIC_EQUALS);
				} else if (node.getTemplate().getNodeType() == QueryNode.NODETYPE_FALDOLOCATION) {
					filter.setType(NodeFilter.FILTER_TYPE_FALDOLOCATION);
				} else if (node.getTemplate().getNodeType() == QueryNode.NODETYPE_GENERICLITERAL) {
					if (NodeFilter.isString(node.getTemplate().getLiteralXsdType())) {
						filter.setType(NodeFilter.FILTER_TYPE_GENERIC_CONTAINS);
					} else {
						filter.setType(NodeFilter.FILTER_TYPE_GENERIC_BETWEEN);
					}
				} else if (node.getTemplate().getNodeType() == QueryNode.NODETYPE_GENERICENTITY || node.getTemplate().getNodeType() == QueryNode.NODETYPE_TYPEDENTITY) {
					filter.setType(NodeFilter.FILTER_TYPE_GENERIC_VALUES);
				}
				
				// end need to fix
				
				switch (filter.getType()) {
				case NodeFilter.FILTER_TYPE_GENERIC_EQUALS:
					if (filter.getNot()) {
						NodeValue value = filter.getValue(node.getTemplate().getLiteralXsdType());
						expr = new E_NotEquals(variable, value);
					} else {
						NodeValue value = filter.getValue(node.getTemplate().getLiteralXsdType());
						expr = new E_Equals(variable, value);
					}
					break;
				case NodeFilter.FILTER_TYPE_GENERIC_BETWEEN:
					Expr min = null;
					Expr max = null;
					Boolean hasMinValue = (null != filter.getMinDouble() 
							|| null != filter.getMinInteger());
					
					Boolean hasMaxValue = (null != filter.getMaxDouble() 
							|| null != filter.getMaxInteger());
					if (hasMinValue) {
						NodeValue value = filter.getMinValue(node.getTemplate().getLiteralXsdType());
						if (filter.getIncludeMin()) {
							min = new E_GreaterThanOrEqual(variable, value);
						} else {
							min = new E_GreaterThan(variable, value);
						}
						expr = min;
					}
					if (hasMaxValue) {
						NodeValue value = filter.getMaxValue(node.getTemplate().getLiteralXsdType());
						if (filter.getIncludeMax()) {
							max = new E_LessThanOrEqual(variable, value);
						} else {
							max = new E_LessThan(variable, value);
						}
						expr = max;
					}
					if (hasMinValue && hasMaxValue) {
						expr = new E_LogicalAnd(min, max);
					}
					if (filter.getNot()) {
						expr = new E_LogicalNot(expr);
					}
					break;
				case NodeFilter.FILTER_TYPE_GENERIC_CONTAINS :
					NodeValue value = filter.getValue(node.getTemplate().getLiteralXsdType());
					if (filter.getCaseInsensitive()) {
						expr = new E_StrContains(new E_StrUpperCase(new E_Str(variable)), new E_StrUpperCase(value));
					} else {
						expr = new E_StrContains(new E_Str(variable), value);	
					}
					if (filter.getNot()) {
						expr = new E_LogicalNot(expr);
					}
					break;
				case NodeFilter.FILTER_TYPE_GENERIC_STARTSWITH :
					value = filter.getValue(node.getTemplate().getLiteralXsdType());
					if (filter.getCaseInsensitive()) {
						expr = new E_StrStartsWith(new E_StrUpperCase(new E_Str(variable)), new E_StrUpperCase(value));
					} else {
						expr = new E_StrContains(new E_Str(variable), value);	
					}
					if (filter.getNot()) {
						expr = new E_LogicalNot(expr);
					}
					break;
				}
				result.add(new ElementFilter(expr));
			}
			// result stays empty for non literal nodes
		} else if (QueryNode.NODETYPE_FALDOLOCATION == node.getNodeType()) {
			Optional<NodeFilter> faldoFilter = node.getNodeFilters().stream().filter(nodeFilter -> NodeFilter.FILTER_TYPE_FALDOLOCATION == nodeFilter.getType()).findFirst();
			for (ElementFilter filter: faldoFilters(variableName, faldoFilter)) {
				result.add(filter);
			}
		}
		return result;
	}
	
	private List<ElementFilter> generateBridgeFilters(Map<QueryNode,String> variableNames, QueryDefinition qd) {
		List<ElementFilter> result = new LinkedList<>();
		for (QueryBridge bridge: qd.getQueryBridges()) {
			String fromVarName = variableNames.get(bridge.getFromNode());
			String toVarName = variableNames.get(bridge.getToNode());
			ExprVar fromVariable = new ExprVar(fromVarName);
			ExprVar toVariable = new ExprVar(toVarName);
			if (QueryBridge.BRIDGE_TYPE_LITERAL_TO_LITERAL == bridge.getType()){
				Expr expr = null;
				switch (bridge.getLiteralToLiteralMatchType()) {
				case QueryBridge.BRIDGE_MATCH_CONTAINS:
					expr = new E_StrContains(new E_Str(fromVariable), new E_Str(toVariable));
					break;
				case QueryBridge.BRIDGE_MATCH_ISCONTAINEDIN:
					expr = new E_StrContains(new E_Str(toVariable), new E_Str(fromVariable));
					break;
				case QueryBridge.BRIDGE_MATCH_STREQUAL:
					expr = new E_Equals(new E_Str(fromVariable), new E_Str(toVariable));
					break;
				case QueryBridge.BRIDGE_MATCH_LESS:
					expr = new E_LessThan(fromVariable, toVariable);
					break;
				case QueryBridge.BRIDGE_MATCH_LESSOREQUAL:
					expr = new E_LessThanOrEqual(fromVariable, toVariable);
					break;
				case QueryBridge.BRIDGE_MATCH_MORE:
					expr = new E_LessThan(toVariable, fromVariable);
					break;
				case QueryBridge.BRIDGE_MATCH_MOREOREQUAL:
					expr = new E_LessThanOrEqual(toVariable, fromVariable);
					break;
				case QueryBridge.BRIDGE_MATCH_EQUAL:
					expr = new E_Equals(fromVariable, toVariable);
					break;
				}
				result.add(new ElementFilter(expr));
			} else if (QueryBridge.BRIDGE_TYPE_LITERAL_TO_ENTITY == bridge.getType()){
				Expr expr = new E_Equals(new E_Str(fromVariable), new E_StrAfter(new NodeValueString(bridge.getStringToEntityTemplate()), new E_Str(toVariable)));
				result.add(new ElementFilter(expr));
			} else if (QueryBridge.BRIDGE_TYPE_ENTITY_TO_LITERAL == bridge.getType()){
				Expr expr = new E_Equals(new E_Str(toVariable), new E_StrAfter(new NodeValueString(bridge.getStringToEntityTemplate()), new E_Str(fromVariable)));
				result.add(new ElementFilter(expr));
			} else if (QueryBridge.BRIDGE_TYPE_LOCATION == bridge.getType()) {
				ExprVar fromBeginPos = new ExprVar(beginPosVar(fromVarName));
				ExprVar fromEndPos = new ExprVar(endPosVar(fromVarName));
				ExprVar toBeginPos = new ExprVar(beginPosVar(toVarName));
				ExprVar toEndPos = new ExprVar(endPosVar(toVarName));
				ExprVar fromRef = new ExprVar(referenceVar(fromVarName));
				ExprVar toRef = new ExprVar(referenceVar(toVarName));
				ExprVar fromStrand = new ExprVar(positionType(fromVarName));
				ExprVar toStrand = new ExprVar(positionType(toVarName));
				result.add(new ElementFilter(new E_LessThanOrEqual(fromBeginPos, toEndPos)));
				result.add(new ElementFilter(new E_LessThanOrEqual(toBeginPos, fromEndPos)));
				result.add(new ElementFilter(new E_SameTerm(fromRef, toRef)));
				if (bridge.getMatchStrand()) {
					result.add(new ElementFilter(new E_SameTerm(fromStrand, toStrand)));
				}
			} else {
				// entity to entity already handled by variable name selection
			}
		}
		return result;
	}
	
	
	
	private Set<Triple> buildInsertTriples(Map<QueryNode, String> variableNames, QueryDefinition query) {
		Map<QueryNode, String> targetNames = eliminateBridges(variableNames, query);
		// check if we have only one cluster
		Set<Set<QueryEdge>> edgeClusters = query.findClusters();
		if (edgeClusters.size() != 1) return null;
		// normal triples
		Set<Triple> triples = new HashSet<>();
		for (QueryGraph graph: query.getQueryGraphs()) {
			for (QueryEdge edge: graph.getQueryEdges().stream().filter(QueryEdge::getRetrieve).collect(Collectors.toSet())) {
				triples.add(new Triple(NodeFactory.createVariable(targetNames.get(edge.getFrom())), NodeFactory.createURI(edge.getTemplate().getTerm()), NodeFactory.createVariable(targetNames.get(edge.getTo()))));
				if (QueryNode.NODETYPE_FALDOLOCATION == edge.getFrom().getTemplate().getNodeType()) {
					triples.addAll(faldoTriples(targetNames.get(edge.getFrom()), Optional.empty()));
				}
				if (QueryNode.NODETYPE_FALDOLOCATION == edge.getTo().getTemplate().getNodeType()) {
					triples.addAll(faldoTriples(targetNames.get(edge.getTo()), Optional.empty()));
				}
			}
		}
		// WILL NOT ALLOW RETRIEVING GRAPHS CROSSING LITERAL TO LITERAL BRIDGES FOR NOW
		// special triples for literal bridges
//		for (QueryBridge bridge: query.getQueryBridges().stream().filter(bridge -> bridge.getFromNode().getNodeType() == QueryNode.NODETYPE_LITERAL && bridge.getToNode().getNodeType() == QueryNode.NODETYPE_LITERAL).collect(Collectors.toSet())) {
//			switch (bridge.getLiteralToLiteralMatchType()) {
//			case QueryBridge.BRIDGE_MATCH_CONTAINS :
//				triples.add(new Triple(NodeFactory.createVariable(targetNames.get(bridge.getFromNode())), BoinqVocab.contains.asNode(), NodeFactory.createVariable(targetNames.get(bridge.getToNode()))));
//				break;
//			case QueryBridge.BRIDGE_MATCH_ISCONTAINEDIN :
//				triples.add(new Triple(NodeFactory.createVariable(targetNames.get(bridge.getToNode())), BoinqVocab.contains.asNode(), NodeFactory.createVariable(targetNames.get(bridge.getFromNode()))));
//				break;
//			}
//		}
		return triples;
	}
	
	
	private GraphTemplate buildGraphTemplate(Map<QueryNode, String> variableNames, QueryDefinition query) {
		GraphTemplate result = new GraphTemplate();
		result.setGraphIri(query.getTargetGraph());
		result.setEdgeTemplates(new HashSet<>());
		result.setEndpointUrl(localSparqlUpdateEndpoint);
		result.setType(GraphTemplate.GRAPH_TYPE_LOCAL);
		// check if we have only one cluster
		Set<Set<QueryEdge>> edgeClusters = query.findClusters();
		if (edgeClusters.size() != 1) return null;
		Set<QueryEdge> retrieveEdges = new HashSet<>();
		for (QueryGraph graph: query.getQueryGraphs()) {
			for (QueryEdge edge: graph.getQueryEdges().stream().filter(QueryEdge::getRetrieve).collect(Collectors.toSet())) {
				retrieveEdges.add(edge);
			}
		}
		Map<QueryNode, NodeTemplate> templateMap = new HashMap<>();
		// first map all nodes onto their own templates
		// then replace those connected by bridge by a common node template
		for (QueryEdge edge: retrieveEdges) {
			templateMap.put(edge.getFrom(), edge.getFrom().getTemplate());
			templateMap.put(edge.getTo(), edge.getTo().getTemplate());
		}
		for (Set<QueryBridge> bridgeHub: query.findBridgeHubs()) {
			// cannot be literal nodes as we do not allow retrieving graphs over literal to literal bridges 
			NodeTemplate merged = new NodeTemplate();
			merged.setColor("red");
			merged.setNodeType(QueryNode.NODETYPE_GENERICENTITY);
			for (QueryBridge bridge: bridgeHub) {
				// only for those in retrieve map
				if (templateMap.containsKey(bridge.getFromNode())) {
					templateMap.put(bridge.getFromNode(), merged);
				}
				if (templateMap.containsKey(bridge.getToNode())) {
					templateMap.put(bridge.getToNode(), merged);
				}
			}
			// when we do: should add literal case
		}
		// now build template
		for (QueryEdge edge: retrieveEdges) {
			EdgeTemplate newEdge = new EdgeTemplate(templateMap.get(edge.getFrom()), templateMap.get(edge.getTo()), edge.getTemplate().getTerm());
			result.getEdgeTemplates().add(newEdge);
		}
		return result;
	}

	private Map<QueryNode, String> eliminateBridges(Map<QueryNode, String> variableNames, QueryDefinition query) {
		Map<QueryNode, String> result = new HashMap<>();
		result.putAll(variableNames);
		for (QueryBridge bridge: query.getQueryBridges()) {
			// what about multiple 
			if (QueryBridge.BRIDGE_TYPE_LITERAL_TO_LITERAL != bridge.getType()) {
				result.put(bridge.getToNode(), variableNames.get(bridge.getToNode()));
			}
		}
		return result;
	}
	
 	// utility
	
	
	private String nextId(String prefix) {
		if (prefix == null) {
			prefix = "";
		}
		if (idCounters.containsKey(prefix)) {
			idCounters.put(prefix, idCounters.get(prefix) + 1);
		} else {
			idCounters.put(prefix, 0L);
		}
		return prefix + idCounters.get(prefix);
	}


}
