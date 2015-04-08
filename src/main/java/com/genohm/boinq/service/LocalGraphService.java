package com.genohm.boinq.service;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.tools.queries.Prefixes;
import com.hp.hpl.jena.graph.Node;
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
	static final private String ENDPOINT_SPARQL = "endpoint.sparql";
	static final private String ENDPOINT_META = "endpoint.meta";
	static final private String ENDPOINT_UPDATE = "endpoint.update";
	static final private String LOCAL_DATASOURCE_URI = "localdatasource";
	static final private String GRAPH_META = "metagraph";
	static final private String GRAPHBASE = "graphbase";
	
	private String graphBase;
	private String updateEndpoint;
	private String sparqlEndpoint;
	private String metaEndpoint;
	private String metaGraph;
	private String localDatasourceUri;
	
	private RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment env) {
		propertyResolver = new RelaxedPropertyResolver(env, PREFIX);
	}
	
	@PostConstruct
	public void init() {
		this.graphBase = propertyResolver.getProperty(GRAPHBASE);
		this.sparqlEndpoint = propertyResolver.getProperty(ENDPOINT_SPARQL);
		this.metaEndpoint = propertyResolver.getProperty(ENDPOINT_META);
		this.updateEndpoint = propertyResolver.getProperty(ENDPOINT_UPDATE);
		this.metaGraph = propertyResolver.getProperty(GRAPH_META);
		this.localDatasourceUri = propertyResolver.getProperty(LOCAL_DATASOURCE_URI);
	}
	
	private void updateMetaGraph(String graphName) {
		QuadDataAcc newData = new QuadDataAcc();
		newData.setGraph(NodeFactory.createURI(this.metaGraph));
		Node graphIRI = NodeFactory.createURI(graphName);
		newData.addTriple(new Triple(graphIRI, RDF.type.asNode(), TrackVocab.Track.asNode()));
		newData.addTriple(new Triple(NodeFactory.createURI(this.localDatasourceUri), TrackVocab.provides.asNode(), graphIRI));
		UpdateDataInsert insertStatement = new UpdateDataInsert(newData);
		UpdateRequest req = new UpdateRequest(insertStatement);
		req.setPrefixMapping(Prefixes.getCommonPrefixes());
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(req, this.updateEndpoint);
		processor.execute();
	}
	
	public String createLocalGraph(String id) {
		if (id == null || id.length() == 0) {
			return createLocalGraph();
		}
		String graphName = graphNameFromId(id);
		updateMetaGraph(graphName);
		return graphName;
	}
	
	public String graphNameFromId(Object id) {
		return this.graphBase + id.toString();
	}
	
	public String createLocalGraph() {
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
