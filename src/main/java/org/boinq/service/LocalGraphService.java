package org.boinq.service;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.modify.request.UpdateDrop;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateProcessor;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
public class LocalGraphService implements EnvironmentAware {
	
@Inject
MetadataGraphService metadataGraphService;


	static final private String PREFIX = "spring.triplestore.";
	
	static final private String LOCAL_ENDPOINT_SPARQL = "endpoint.data.query";
	static final private String LOCAL_ENDPOINT_UPDATE = "endpoint.data.update";
	static final private String LOCAL_ENDPOINT_META = "endpoint.meta.query";
	static final private String LOCAL_ENDPOINT_META_UPDATE = "endpoint.meta.update";

	static final private String LOCAL_DATASOURCE_URI = "localdatasource";
	static final private String GRAPH_META = "metagraph";
	static final private String GRAPHBASE = "graphbase";
	
	private String graphBase;
	private String sparqlEndpoint;
	private String updateEndpoint;
	private String metaEndpoint;
	private String metaUpdateEndpoint;
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
		this.sparqlEndpoint = propertyResolver.getProperty(LOCAL_ENDPOINT_SPARQL);
		this.updateEndpoint = propertyResolver.getProperty(LOCAL_ENDPOINT_UPDATE);
		this.metaEndpoint = propertyResolver.getProperty(LOCAL_ENDPOINT_META);
		this.metaUpdateEndpoint = propertyResolver.getProperty(LOCAL_ENDPOINT_META_UPDATE);
		this.metaGraph = propertyResolver.getProperty(GRAPH_META);
		this.localDatasourceUri = propertyResolver.getProperty(LOCAL_DATASOURCE_URI);
	}
	
	
	public void deleteGraph(String graphName) {
		UpdateDrop req = new UpdateDrop(NodeFactory.createURI(graphName));
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(req, updateEndpoint);
		processor.execute();
	}
	
	public void deleteLocalGraph(String id) {
		deleteGraph(graphNameFromId(id));
	}
	
	public String createLocalGraph(String id) {
		if (id == null || id.length() == 0) {
			return createLocalGraph();
		}
		String graphName = graphNameFromId(id);
		metadataGraphService.updateTrackCreation(graphName, this.metaGraph, this.localDatasourceUri , this.updateEndpoint);
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

	public String getMetaUpdateEndpoint() {
		return metaUpdateEndpoint;
	}
	
	public String getMetaGraph() {
		return metaGraph;
	}
	
	
	
}
