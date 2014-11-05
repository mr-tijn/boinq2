package com.genohm.boinq.service;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.GraphDescriptor;

@Service
public class LocalGraphService implements EnvironmentAware {

	static final private String PREFIX = "spring.tripleupload.";
	static final private String ENDPOINT_UPDATE = "endpoint.update";
	static final private String ENDPOINT_SPARQL = "endpoint.sparql";
	static final private String GRAPHBASE = "graphbase";
	
	private String graphBase;
	private String updateEndpoint;
	private String sparqlEndpoint;
	
	private RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment env) {
		propertyResolver = new RelaxedPropertyResolver(env, PREFIX);
	}
	
	@PostConstruct
	public void init() {
		this.graphBase = propertyResolver.getProperty(GRAPHBASE);
		this.updateEndpoint = propertyResolver.getProperty(ENDPOINT_UPDATE);
		this.sparqlEndpoint = propertyResolver.getProperty(ENDPOINT_SPARQL);
	}
	
	public GraphDescriptor createLocalGraph(String id) {
		GraphDescriptor newGraph = new GraphDescriptor();
		newGraph.endpointURI = this.sparqlEndpoint;
		newGraph.endpointUpdateURI = this.updateEndpoint;
		newGraph.graphURI = this.graphBase + id;
		return newGraph;
	}
	
	public GraphDescriptor createLocalGraph() {
		return createLocalGraph(UUID.randomUUID().toString());
	}
	
}
