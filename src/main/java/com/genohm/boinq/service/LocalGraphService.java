package com.genohm.boinq.service;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.GraphDescriptor;
import com.genohm.boinq.tools.vocabularies.DatasourceVocabulary;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.vocabulary.RDF;

@Service
public class LocalGraphService implements EnvironmentAware {

	static final private String PREFIX = "spring.tripleupload.";
	static final private String ENDPOINT_UPDATE = "endpoint.update";
	static final private String ENDPOINT_SPARQL = "endpoint.sparql";
	static final private String ENDPOINT_META = "endpoint.meta";
	static final private String GRAPH_META = "metagraph";
	static final private String GRAPHBASE = "graphbase";
	
	private String graphBase;
	private String updateEndpoint;
	private String sparqlEndpoint;
	private String metaEndpoint;
	private String metaGraph;
	
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
		this.metaEndpoint = propertyResolver.getProperty(ENDPOINT_META);
		this.metaGraph = propertyResolver.getProperty(GRAPH_META);
	}
	
	private void updateMetaGraph(GraphDescriptor graph) {
		QuadDataAcc newData = new QuadDataAcc();
		newData.setGraph(NodeFactory.createURI(graph.metaGraphURI));
		newData.addTriple(new Triple(NodeFactory.createURI(graph.graphURI), RDF.type.asNode(), DatasourceVocabulary.datasource));
		UpdateDataInsert insertStatement = new UpdateDataInsert(newData);
		UpdateRequest req = new UpdateRequest(insertStatement);
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(req, graph.metaEndpointURI);
		processor.execute();
	}
	
	public GraphDescriptor createLocalGraph(String id) {
		GraphDescriptor newGraph = new GraphDescriptor();
		newGraph.endpointURI = this.sparqlEndpoint;
		newGraph.endpointUpdateURI = this.updateEndpoint;
		newGraph.graphURI = this.graphBase + id;
		newGraph.metaEndpointURI = this.metaEndpoint;
		newGraph.metaGraphURI = this.metaGraph;
		updateMetaGraph(newGraph);
		return newGraph;
	}
	
	public GraphDescriptor createLocalGraph() {
		return createLocalGraph(UUID.randomUUID().toString());
	}
	
}
