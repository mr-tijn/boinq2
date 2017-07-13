package com.genohm.boinq.web.rest;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.query.EdgeTemplate;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.domain.query.NodeTemplate;
import com.genohm.boinq.repository.GraphTemplateRepository;
import com.genohm.boinq.web.rest.dto.EdgeTemplateDTO;
import com.genohm.boinq.web.rest.dto.GraphTemplateDTO;
import com.genohm.boinq.web.rest.dto.NodeTemplateDTO;

@RestController
@RequestMapping("/app/rest/")
public class GraphTemplateResource {
	
	@Inject
	private GraphTemplateRepository graphTemplateRepository;

    @RequestMapping(value = "graphtemplate/{gtId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public GraphTemplateDTO get(Principal principal, @PathVariable Long gtId) throws Exception {
    	Optional<GraphTemplate> graphTemplate = graphTemplateRepository.findOneById(gtId);
    	if (graphTemplate.isPresent()) {
    		GraphTemplateDTO result = GraphTemplateDTO.create(graphTemplate.get()); 
    		return result;
    	} else {
    		throw new RuntimeException("Cannot get graphtemplate "+gtId);
    	}
//     	return graphTemplateRepository
//    			.findOneById(gtId)
//    			.map(GraphTemplateDTO::create)
//    			.orElseThrow(() -> new RuntimeException("Cannot get graphtemplate "+gtId));
    }
    
    
    @RequestMapping(value = "graphtemplate",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GraphTemplate> getAll(Principal principal) throws Exception {
    	return graphTemplateRepository
    			.findAll();
    }
    
    @RequestMapping(value = "graphtemplate/{gtId}",
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public GraphTemplate save(Principal principal, @RequestBody GraphTemplateDTO template) throws Exception {
    	GraphTemplate gt = new GraphTemplate();
    	if (template.id() != null) {
    		Optional<GraphTemplate> templateOpt = graphTemplateRepository.findOneById(template.id());
    		if (templateOpt.isPresent()) {
    			gt = templateOpt.get();
    		}
    	} 
    	// general
    	gt.setEndpointUrl(template.endpointUrl());
    	gt.setGraphIri(template.graphIri());
    	gt.setName(template.name());
    	gt.setType(template.type());
   		// edges
    	Set<EdgeTemplate> edgeTemplates = new HashSet<EdgeTemplate>();
		List<NodeTemplate> nodeTemplates = gt.getEdgeTemplates().stream().map(EdgeTemplate::getFrom).collect(Collectors.toList());
		nodeTemplates.addAll(gt.getEdgeTemplates().stream().map(EdgeTemplate::getTo).collect(Collectors.toList()));
    	for (EdgeTemplateDTO edge: template.edgeTemplates()) {
    		EdgeTemplate et = new EdgeTemplate();
    		NodeTemplate ft = new NodeTemplate();
    		NodeTemplate tt = new NodeTemplate();
    		if (edge.id() != null) {
    			Optional<EdgeTemplate> edgeOpt = gt.getEdgeTemplates().stream().filter(e -> e.getId() == edge.id()).findFirst();
    			if (edgeOpt.isPresent()) {
    				et = edgeOpt.get();
    			}
    		}
    		et.setLabel(edge.label());
    		et.setTerm(edge.term());
    		NodeTemplateDTO fromNode = template.nodeTemplates().stream().filter(n -> n.idx() == edge.fromIdx()).findFirst().get();
    		if (fromNode.id() != null) {
    			Optional<NodeTemplate> nodeOpt = nodeTemplates.stream().filter(n -> n.getId() == fromNode.id()).findFirst();
    			if (nodeOpt.isPresent()) {
    				ft = nodeOpt.get();
    			}
    		}
    		ft.setAssembly(fromNode.assembly());
    		ft.setColor(fromNode.color());
    		ft.setDescription(fromNode.description());
    		ft.setFilterable(fromNode.filterable());
    		ft.setFixedType(fromNode.fixedType());
    		ft.setFixedValue(fromNode.fixedValue());
    		ft.setIdx(fromNode.idx());
    		ft.setLiteralXsdType(fromNode.literalXsdType());
    		ft.setName(fromNode.name());
    		ft.setNodeType(fromNode.nodeType());
    		ft.setValuesEndpoint(fromNode.valuesEndpoint());
    		ft.setValuesGraph(fromNode.valuesGraph());
    		ft.setValueSource(fromNode.valueSource());
    		ft.setValuesRootTerm(fromNode.valuesRootTerm());
    		ft.setVariablePrefix(fromNode.variablePrefix());
    		ft.setX(fromNode.x());
    		ft.setY(fromNode.y());
    		NodeTemplateDTO toNode = template.nodeTemplates().stream().filter(n -> n.idx() == edge.toIdx()).findFirst().get();
    		if (toNode.id() != null) {
    			Optional<NodeTemplate> nodeOpt = nodeTemplates.stream().filter(n -> n.getId() == toNode.id()).findFirst();
    			if (nodeOpt.isPresent()) {
    				tt = nodeOpt.get();
    			}
    		}
    		tt.setAssembly(toNode.assembly());
    		tt.setColor(toNode.color());
    		tt.setDescription(toNode.description());
    		tt.setFilterable(toNode.filterable());
    		tt.setFixedType(toNode.fixedType());
    		tt.setFixedValue(toNode.fixedValue());
    		tt.setIdx(toNode.idx());
    		tt.setLiteralXsdType(toNode.literalXsdType());
    		tt.setName(toNode.name());
    		tt.setNodeType(toNode.nodeType());
    		tt.setValuesEndpoint(toNode.valuesEndpoint());
    		tt.setValuesGraph(toNode.valuesGraph());
    		tt.setValueSource(toNode.valueSource());
    		tt.setValuesRootTerm(toNode.valuesRootTerm());
    		tt.setVariablePrefix(toNode.variablePrefix());
    		tt.setX(toNode.x());
    		tt.setY(toNode.y());
    		et.setFrom(ft);
    		et.setTo(tt);
    		edgeTemplates.add(et);
    	}
    	gt.setEdgeTemplates(edgeTemplates);
    	return graphTemplateRepository.save(gt);
    }
}
