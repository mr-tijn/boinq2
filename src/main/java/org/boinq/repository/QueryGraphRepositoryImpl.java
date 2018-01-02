package org.boinq.repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.boinq.domain.query.EdgeTemplate;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.NodeFilter;
import org.boinq.domain.query.NodeTemplate;
import org.boinq.domain.query.QueryEdge;
import org.boinq.domain.query.QueryGraph;
import org.boinq.domain.query.QueryNode;
import org.boinq.web.rest.dto.NodeFilterDTO;
import org.boinq.web.rest.dto.QueryEdgeDTO;
import org.boinq.web.rest.dto.QueryGraphDTO;
import org.boinq.web.rest.dto.QueryNodeDTO;

public class QueryGraphRepositoryImpl implements QueryGraphRepositoryExtensions {

	@Inject
	GraphTemplateRepository graphTemplateRepository;
	
	
	private QueryNodeDTO findIdx(List<QueryNodeDTO> nodes, Integer idx) {
		return nodes.stream().filter(node -> node.idx().equals(idx)).findFirst().orElse(null);
	}
	
	private EdgeTemplate findEdge(Set<EdgeTemplate> edges, Long id) {
		return edges.stream().filter(edge -> edge.getId().equals(id)).findFirst().orElse(null);
	}
	
	private NodeTemplate findNode(Set<NodeTemplate> nodes, Long id) {
		return nodes.stream().filter(node -> node.getId().equals(id)).findFirst().orElse(null);
	}
	
	@Override
	public QueryGraph create(QueryGraphDTO graphDTO) {
		QueryGraph graph = new QueryGraph();
		graph.setId(graphDTO.id());
		GraphTemplate template = graphTemplateRepository.findOneById(graphDTO.graphTemplateId()).orElse(null);
		graph.setTemplate(template);
		Set<QueryEdge> edges = graphDTO.queryEdges().stream().map(edgeDTO -> create(edgeDTO, findIdx(graphDTO.queryNodes(), edgeDTO.fromIdx()), findIdx(graphDTO.queryNodes(), edgeDTO.toIdx()), template)).collect(Collectors.toSet());
		graph.setQueryEdges(edges);
		return graph;
	}

	public QueryEdge create(QueryEdgeDTO edgeDTO, QueryNodeDTO fromNodeDTO, QueryNodeDTO toNodeDTO, GraphTemplate template) {
		QueryEdge edge = new QueryEdge();
		edge.setFrom(create(fromNodeDTO, template));
		edge.setTo(create(toNodeDTO, template));
		edge.setRetrieve(edgeDTO.retrieve());
		edge.setTemplate(findEdge(template.getEdgeTemplates(), edgeDTO.templateId()));
		return edge;
	}

	public QueryNode create(QueryNodeDTO nodeDTO, GraphTemplate template) {
		QueryNode node = new QueryNode();
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
	
}
