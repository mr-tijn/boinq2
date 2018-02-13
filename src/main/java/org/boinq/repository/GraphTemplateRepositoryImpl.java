package org.boinq.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.boinq.domain.Datasource;
import org.boinq.domain.Track;
import org.boinq.domain.query.EdgeTemplate;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.NodeTemplate;
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
	@Inject
	DatasourceRepository datasourceRepository;
	@Inject
	TrackRepository trackRepository;
	
	private NodeTemplate findByIdx(Set<NodeTemplate> nodes, Integer idx) {
		return nodes.stream().filter(node -> node.getIdx().equals(idx)).findFirst().orElse(null);
	}
	
	private EdgeTemplate create(EdgeTemplateDTO edgeDTO, NodeTemplate from, NodeTemplate to) {
		EdgeTemplate result = new EdgeTemplate();
		if (edgeDTO.id() != null) {
			Optional<EdgeTemplate> et = edgeTemplateRepository.findOneById(edgeDTO.id());
			if (et.isPresent()) {
				result = et.get();
			}
		}
		result.setLabel(edgeDTO.label());
		result.setTerm(edgeDTO.term());
		result.setFrom(from);
		result.setTo(to);
		return result;
	}
	
	private NodeTemplate create(NodeTemplateDTO nodeDTO) {
		NodeTemplate result = new NodeTemplate();
		if (nodeDTO.id() != null) {
			Optional<NodeTemplate> nt = nodeTemplateRepository.findOneById(nodeDTO.id());
			if (nt.isPresent()) {
				result = nt.get();
			}
		}
		result.setAssembly(nodeDTO.assembly());
		result.setColor(nodeDTO.color());
		result.setDescription(nodeDTO.description());
		result.setFilterable(nodeDTO.filterable());
		result.setFixedValue(nodeDTO.fixedValue());
		result.setFixedType(nodeDTO.fixedType());
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
		if (templateDTO.id() != null) {
			Optional<GraphTemplate> gt = graphTemplateRepository.findOneById(templateDTO.id());
			if (gt.isPresent()) {
				result = gt.get();
			}
		}
		result.setEndpointUrl(templateDTO.endpointUrl());
		result.setGraphIri(templateDTO.graphIri());
		result.setType(templateDTO.type());
		result.setName(templateDTO.name());
		// TODO: type, endpoint and graph IRI is currently saved in track and datasource: change in editor
		Optional<Track> trackOpt = trackRepository.findOneByGraphTemplateId(templateDTO.id());
		if (trackOpt.isPresent()) {
			Optional<Datasource> dsOpt = datasourceRepository.findOneByTracksId(trackOpt.get().getId());
			if (dsOpt.isPresent()) {
				switch (dsOpt.get().getType()) {
				case Datasource.TYPE_LOCAL:
					result.setType(GraphTemplate.GRAPH_TYPE_LOCAL);
					break;
				default:
					result.setType(GraphTemplate.GRAPH_TYPE_REMOTE);
				}
				result.setEndpointUrl(dsOpt.get().getEndpointUrl());
				result.setGraphIri(trackOpt.get().getGraphName());
			}
		}
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
