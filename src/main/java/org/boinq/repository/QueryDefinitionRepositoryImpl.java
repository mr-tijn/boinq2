package org.boinq.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.boinq.domain.User;
import org.boinq.domain.query.EdgeTemplate;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.NodeFilter;
import org.boinq.domain.query.NodeTemplate;
import org.boinq.domain.query.QueryBridge;
import org.boinq.domain.query.QueryDefinition;
import org.boinq.domain.query.QueryEdge;
import org.boinq.domain.query.QueryGraph;
import org.boinq.domain.query.QueryNode;
import org.boinq.web.rest.dto.NodeFilterDTO;
import org.boinq.web.rest.dto.QueryBridgeDTO;
import org.boinq.web.rest.dto.QueryDefinitionDTO;
import org.boinq.web.rest.dto.QueryEdgeDTO;
import org.boinq.web.rest.dto.QueryGraphDTO;
import org.boinq.web.rest.dto.QueryNodeDTO;

public class QueryDefinitionRepositoryImpl implements QueryDefinitionRepositoryExtensions {

	@Inject
	GraphTemplateRepository graphTemplateRepository;
	@Inject
	QueryDefinitionRepository queryDefinitionRepository;
	@Inject
	QueryGraphRepository queryGraphRepository;
	@Inject
	QueryNodeRepository queryNodeRepository;
	@Inject
	QueryEdgeRepository queryEdgeRepository;
	@Inject
	QueryBridgeRepository queryBridgeRepository;
	@Inject
	NodeFilterRepository nodeFilterRepository;
	@Inject
	UserRepository userRepository;
	
	private QueryNode findNodeByIdx(Set<QueryNode> nodes, Integer idx) {
		return nodes.stream().filter(node -> idx.equals(node.getIdx())).findFirst().orElse(null);
	}
	
	private EdgeTemplate findEdge(Set<EdgeTemplate> edges, Long id) {
		return edges.stream().filter(edge -> id.equals(edge.getId())).findFirst().orElse(null);
	}
	
	private NodeTemplate findNode(Set<NodeTemplate> nodes, Long id) {
		return nodes.stream().filter(node -> id.equals(node.getId())).findFirst().orElse(null);
	}
	
	private QueryGraph findGraphByIdx(Set<QueryGraph> graphs, Integer idx) {
		return graphs.stream().filter(graph -> idx.equals(graph.getIdx())).findFirst().orElse(null);
	}
	
	public QueryGraph create(QueryGraphDTO graphDTO) {
		QueryGraph graph = new QueryGraph();
		if (graphDTO.id() != null) {
			Optional<QueryGraph> qgOpt = queryGraphRepository.findOneById(graphDTO.id());
			if (qgOpt.isPresent()) {
				graph = qgOpt.get();
			}
		}
		graph.setId(graphDTO.id());
		graph.setIdx(graphDTO.idx());
		graph.setName(graphDTO.name());
		graph.setX(graphDTO.x());
		graph.setY(graphDTO.y());
		GraphTemplate template = graphTemplateRepository.findOneById(graphDTO.graphTemplateId()).orElse(null);
		graph.setTemplate(template);
		Set<QueryNode> nodes = graphDTO.queryNodes().stream().map(nodeDTO -> create(nodeDTO,template)).collect(Collectors.toSet());
		Set<QueryEdge> edges = graphDTO.queryEdges().stream().map(edgeDTO -> create(edgeDTO, findNodeByIdx(nodes, edgeDTO.fromIdx()), findNodeByIdx(nodes, edgeDTO.toIdx()), template)).collect(Collectors.toSet());
		graph.setQueryEdges(edges);
		return graph;
	}
	
	public QueryBridge create(QueryBridgeDTO bridgeDTO, Set<QueryGraph> graphs) {
		QueryBridge bridge = new QueryBridge();
		if (bridgeDTO.id() != null) {
			Optional<QueryBridge> qbOpt = queryBridgeRepository.findOneById(bridgeDTO.id());
			if (qbOpt.isPresent()) {
				bridge = qbOpt.get();
			}
		}
		bridge.setId(bridgeDTO.id());
		bridge.setLiteralToLiteralMatchType(bridgeDTO.literalToLiteralMatchType());
		bridge.setStringToEntityTemplate(bridgeDTO.stringToEntityTemplate());
		bridge.setMatchStrand(bridgeDTO.matchStrand());
		QueryGraph fromGraph = findGraphByIdx(graphs, bridgeDTO.fromGraphIdx());
		QueryGraph toGraph = findGraphByIdx(graphs, bridgeDTO.toGraphIdx());
		Set<QueryNode> nodeCandidates = fromGraph.getQueryEdges().stream().map(edge -> edge.getFrom()).collect(Collectors.toSet());
		nodeCandidates.addAll(fromGraph.getQueryEdges().stream().map(edge-> edge.getTo()).collect(Collectors.toSet()));
		QueryNode fromNode = findNodeByIdx(nodeCandidates, bridgeDTO.fromNodeIdx());
		nodeCandidates = toGraph.getQueryEdges().stream().map(edge -> edge.getFrom()).collect(Collectors.toSet());
		nodeCandidates.addAll(toGraph.getQueryEdges().stream().map(edge -> edge.getTo()).collect(Collectors.toSet()));
		QueryNode toNode = findNodeByIdx(nodeCandidates, bridgeDTO.toNodeIdx());
		bridge.setFromGraph(fromGraph);
		bridge.setToGraph(toGraph);
		bridge.setFromNode(fromNode);
		bridge.setToNode(toNode);
		return bridge;
	}

	public QueryEdge create(QueryEdgeDTO edgeDTO, QueryNode fromNode, QueryNode toNode, GraphTemplate template) {
		QueryEdge edge = new QueryEdge();
		if (edgeDTO.id() != null) {
			Optional<QueryEdge> qeOpt = queryEdgeRepository.findOneById(edgeDTO.id());
			if (qeOpt.isPresent()) {
				edge = qeOpt.get();
			}
		}
		edge.setFrom(fromNode);
		edge.setTo(toNode);
		// currently all edges are retrieved
		edge.setRetrieve(true);
		edge.setTemplate(findEdge(template.getEdgeTemplates(), edgeDTO.templateId()));
		return edge;
	}

	public QueryNode create(QueryNodeDTO nodeDTO, GraphTemplate template) {
		QueryNode node = new QueryNode();
		if (nodeDTO.id() != null) {
			Optional<QueryNode> qnOpt = queryNodeRepository.findOneById(nodeDTO.id());
			if (qnOpt.isPresent()) {
				node = qnOpt.get();
			}
		}
		node.setId(nodeDTO.id());
		node.setIdx(nodeDTO.idx());
		node.setEntityValues(nodeDTO.entityValues());
		Set<NodeTemplate> nodes = template.getEdgeTemplates().stream().map(edge -> edge.getFrom()).collect(Collectors.toSet());
		nodes.addAll(template.getEdgeTemplates().stream().map(edge -> edge.getTo()).collect(Collectors.toSet()));
		node.setTemplate(findNode(nodes, nodeDTO.templateId()));
		node.setNodeFilters(nodeDTO.nodeFilters().stream().map(filter -> create(filter)).collect(Collectors.toSet()));
		return node;
	}
	
	public NodeFilter create(NodeFilterDTO filterDTO) {
		NodeFilter filter = new NodeFilter();
		if (filterDTO.id() != null) {
			Optional<NodeFilter> nfOpt = nodeFilterRepository.findOneById(filterDTO.id());
			if (nfOpt.isPresent()) {
				filter = nfOpt.get();
			}
		}
		filter.setId(filterDTO.id());
		filter.setType(filterDTO.type());
		filter.setMinInteger(filterDTO.minInteger());
		filter.setMaxInteger(filterDTO.maxInteger());
		filter.setIntegerValue(filterDTO.integerValue());
		filter.setMinDouble(filterDTO.minDouble());
		filter.setMaxDouble(filterDTO.maxDouble());
		filter.setDoubleValue(filterDTO.doubleValue());
		filter.setIncludeMin(filterDTO.includeMin());
		filter.setIncludeMax(filterDTO.includeMax());
		filter.setStringValue(filterDTO.stringValue());
		filter.setCaseInsensitive(filterDTO.caseInsensitive());
		filter.setTermValues(filterDTO.termValues());
		filter.setContig(filterDTO.contig());
		filter.setNot(filterDTO.not());
		return filter;
	}
	
	@Override
	public QueryDefinition create(QueryDefinitionDTO definitionDTO) {
		QueryDefinition queryDefinition = new QueryDefinition();
		if (definitionDTO.id() != null) {
			Optional<QueryDefinition> qdOpt = queryDefinitionRepository.findOneById(definitionDTO.id());
			if (qdOpt.isPresent()) {
				queryDefinition = qdOpt.get();
			}
		}
		queryDefinition.setId(definitionDTO.id());
		queryDefinition.setName(definitionDTO.name());
		queryDefinition.setDescription(definitionDTO.description());
		queryDefinition.setSpecies(definitionDTO.species());
		queryDefinition.setAssembly(definitionDTO.assembly());
		queryDefinition.setStatus(definitionDTO.status());
		queryDefinition.setResultAsTable(definitionDTO.resultAsTable());
		queryDefinition.setTargetGraph(definitionDTO.targetGraph());
		queryDefinition.setTargetFile(definitionDTO.targetFile());
		queryDefinition.setSparqlQuery(definitionDTO.sparqlQuery());
		Optional<User> user = userRepository.findOneByLogin(definitionDTO.owner());
		if (user.isPresent()) {
			queryDefinition.setOwner(user.get());
		}
		Set<QueryGraph> queryGraphs = definitionDTO.queryGraphs().stream().map(graphDTO -> create(graphDTO)).collect(Collectors.toSet());
		Set<QueryBridge> queryBridges = definitionDTO.queryBridges().stream().map(bridgeDTO -> create(bridgeDTO, queryGraphs)).collect(Collectors.toSet());
		queryDefinition.setQueryGraphs(queryGraphs);
		queryDefinition.setQueryBridges(queryBridges);
		return queryDefinition;
	}

	@Override
	@Transactional
	public QueryDefinition deepsave(QueryDefinition definition) {
		Map<QueryNode, QueryNode> savedNodeMap = new HashMap<>();
		Set<QueryGraph> savedGraphs = definition.getQueryGraphs().stream().map(graph -> save(graph, savedNodeMap)).collect(Collectors.toSet());
		Set<QueryBridge> savedBridges = definition.getQueryBridges().stream().map(bridge -> save(bridge, savedNodeMap, savedGraphs)).collect(Collectors.toSet());
		definition.setQueryGraphs(savedGraphs);
		definition.setQueryBridges(savedBridges);
		return queryDefinitionRepository.save(definition);
	}
	
	@Transactional
	private QueryBridge save(QueryBridge bridge, Map<QueryNode, QueryNode> savedNodes, Set<QueryGraph> savedGraphs) {
		bridge.setFromGraph(savedGraphs.stream().filter(graph -> bridge.getFromGraph().getIdx().equals(graph.getIdx())).findFirst().get());
		bridge.setToGraph(savedGraphs.stream().filter(graph -> bridge.getToGraph().getIdx().equals(graph.getIdx())).findFirst().get());
		bridge.setFromNode(save(bridge.getFromNode(), savedNodes));
		bridge.setToNode(save(bridge.getToNode(), savedNodes));
		return queryBridgeRepository.save(bridge);
	}
	
	@Transactional
	private QueryGraph save(QueryGraph graph, Map<QueryNode, QueryNode> savedNodes) {
		Set<QueryEdge> savedEdges = graph.getQueryEdges().stream().map(edge -> save(edge, savedNodes)).collect(Collectors.toSet());
		graph.setQueryEdges(savedEdges);
		QueryGraph savedGraph = queryGraphRepository.save(graph);
		return savedGraph;
	}
	
	@Transactional
	private QueryEdge save(QueryEdge edge, Map<QueryNode, QueryNode> savedNodes) {
		edge.setFrom(save(edge.getFrom(), savedNodes));
		edge.setTo(save(edge.getTo(), savedNodes));
		return queryEdgeRepository.save(edge);
	}
	
	@Transactional
	private QueryNode save(QueryNode node, Map<QueryNode, QueryNode> savedNodes) {
		if (savedNodes.containsKey(node)) {
			return savedNodes.get(node);
		} else {
			node.setNodeFilters(node.getNodeFilters().stream().map(filter -> save(filter)).collect(Collectors.toSet()));
			QueryNode savedNode = queryNodeRepository.save(node);
			savedNodes.put(node, savedNode);
			return savedNode;
		}
	}
	
	@Transactional
	private NodeFilter save(NodeFilter filter) {
		return nodeFilterRepository.save(filter);
	}

}
