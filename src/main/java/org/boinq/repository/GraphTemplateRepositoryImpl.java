package org.boinq.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.boinq.domain.query.EdgeTemplate;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.NodeTemplate;
import org.boinq.domain.query.QueryBridge;
import org.boinq.domain.query.QueryDefinition;
import org.boinq.domain.query.QueryGraph;
import org.boinq.domain.query.QueryNode;
import org.boinq.web.rest.dto.EdgeTemplateDTO;
import org.boinq.web.rest.dto.GraphTemplateDTO;
import org.boinq.web.rest.dto.NodeTemplateDTO;

public class GraphTemplateRepositoryImpl implements GraphTemplateRepositoryExtensions {
	
	@Inject
	NodeTemplateRepository nodeTemplateRepository;
	@Inject
	EdgeTemplateRepository edgeTemplateRepository;
	@Inject
	GraphTemplateRepository graphTemplateRepository;
	
	private NodeTemplate findByIdx(Set<NodeTemplate> nodes, Integer idx) {
		return nodes.stream().filter(node -> node.getIdx().equals(idx)).findFirst().orElse(null);
	}
	
	private EdgeTemplate create(EdgeTemplateDTO edgeDTO, NodeTemplate from, NodeTemplate to) {
		EdgeTemplate result = new EdgeTemplate();
		result.setLabel(edgeDTO.label());
		result.setTerm(edgeDTO.term());
		result.setFrom(from);
		result.setTo(to);
		return result;
	}
	
	private NodeTemplate create(NodeTemplateDTO nodeDTO) {
		NodeTemplate result = new NodeTemplate();
		result.setAssembly(nodeDTO.assembly());
		result.setColor(nodeDTO.color());
		result.setDescription(nodeDTO.description());
		result.setFilterable(nodeDTO.filterable());
		result.setFixedValue(nodeDTO.fixedValue());
		result.setIdx(nodeDTO.idx());
		result.setLiteralXsdType(nodeDTO.literalXsdType());
		result.setName(nodeDTO.name());
		result.setNodeType(nodeDTO.nodeType());
		result.setValuesEndpoint(nodeDTO.valuesEndpoint());
		result.setValuesGraph(nodeDTO.valuesGraph());
		result.setValueSource(nodeDTO.valueSource());
		result.setValuesRootTerm(nodeDTO.valuesRootTerm());
		result.setValuesTermList(nodeDTO.valuesTermList());
		result.setVariablePrefix(nodeDTO.variablePrefix());
		result.setX(nodeDTO.x());
		result.setY(nodeDTO.y());
		return result;
	}
	
	@Override
	public GraphTemplate create(GraphTemplateDTO templateDTO) {
		GraphTemplate result = new GraphTemplate();
		result.setEndpointUrl(templateDTO.endpointUrl());
		result.setGraphIri(templateDTO.graphIri());
		result.setName(templateDTO.name());
		result.setType(templateDTO.type());
		Set<NodeTemplate> nodeTemplates = templateDTO.nodeTemplates().stream().map(nodeDTO -> create(nodeDTO)).collect(Collectors.toSet());
		Set<EdgeTemplate> edgeTemplates =  templateDTO.edgeTemplates().stream().map(edgeDTO -> create(edgeDTO, findByIdx(nodeTemplates, edgeDTO.fromIdx()), findByIdx(nodeTemplates, edgeDTO.toIdx()))).collect(Collectors.toSet());
		result.setEdgeTemplates(edgeTemplates);
		return result;
	}

	@Override
	@Transactional
	public GraphTemplate deepsave(GraphTemplate template) {
		Map<NodeTemplate, NodeTemplate> savedNodes = new HashMap<>();
		Set<NodeTemplate> nodes = template.getEdgeTemplates().stream().map(edge -> edge.getTo()).collect(Collectors.toSet());
		nodes.addAll(template.getEdgeTemplates().stream().map(edge -> edge.getFrom()).collect(Collectors.toSet()));
		nodes.stream().forEach(node -> save(node, savedNodes));
		template.setEdgeTemplates(template.getEdgeTemplates().stream().map(edge -> save(edge, savedNodes)).collect(Collectors.toSet()));
		return graphTemplateRepository.save(template);
	}
	
	@Transactional
	private EdgeTemplate save(EdgeTemplate edge, Map<NodeTemplate, NodeTemplate> savedNodes) {
		edge.setFrom(savedNodes.get(edge.getFrom()));
		edge.setTo(savedNodes.get(edge.getTo()));
		return edgeTemplateRepository.save(edge);
	}
	
	@Transactional
	private NodeTemplate save(NodeTemplate node, Map<NodeTemplate, NodeTemplate> savedNodes) {
		if (savedNodes.containsKey(node)) {
			return savedNodes.get(node);
		} else {
			NodeTemplate saved = nodeTemplateRepository.save(node);
			savedNodes.put(node, saved);
			return saved;
		}
	}

}
