package com.genohm.boinq.service;

import java.util.UUID;

import javax.annotation.PostConstruct;

import net.wimpi.telnetd.io.terminal.ansi;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.GraphDescriptor;
import com.genohm.boinq.tools.queries.Prefixes;
import com.genohm.boinq.tools.vocabularies.TrackVocabulary;
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
		newData.addTriple(new Triple(NodeFactory.createURI(graph.graphURI), RDF.type.asNode(), TrackVocabulary.Datasource));
		UpdateDataInsert insertStatement = new UpdateDataInsert(newData);
		UpdateRequest req = new UpdateRequest(insertStatement);
		req.setPrefixMapping(Prefixes.getCommonPrefixes());
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(req, graph.metaEndpointURI);
		processor.execute();
	}
	
	public GraphDescriptor createLocalGraph(String id) {
		GraphDescriptor newGraph = new GraphDescriptor();
		newGraph.endpointURI = this.sparqlEndpoint;
		newGraph.endpointUpdateURI = this.updateEndpoint;
		newGraph.graphURI = graphNameFromId(id);
		newGraph.metaEndpointURI = this.metaEndpoint;
		newGraph.metaGraphURI = this.metaGraph;
		updateMetaGraph(newGraph);
		return newGraph;
	}
	
	public String graphNameFromId(Object id) {
		return this.graphBase + id.toString();
	}
	
	public GraphDescriptor createLocalGraph() {
		return createLocalGraph(UUID.randomUUID().toString());
	}

	public String getGraphBase() {
		return graphBase;
	}

	public String getUpdateEndpoint() {
		return updateEndpoint;
	}

	public String getSparqlEndpoint() {
		return sparqlEndpoint;
	}

	public String getMetaEndpoint() {
		return metaEndpoint;
	}

	public String getMetaGraph() {
		return metaGraph;
	}
	
	
	
}
