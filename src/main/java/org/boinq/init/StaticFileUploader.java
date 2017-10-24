package org.boinq.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.modify.request.UpdateCreate;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.update.UpdateExecutionFactory;
import org.boinq.domain.SPARQLResultSet;
import org.boinq.service.SPARQLClientService;
import org.boinq.service.TripleUploadService;
import org.boinq.service.TripleUploadService.TripleUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class StaticFileUploader implements ApplicationListener<ContextRefreshedEvent> {

	private static final String SO_URI = "http://purl.obolibrary.org/obo/so-xp.obo";
	private static final String GO_URI = "http://purl.obolibrary.org/obo/go.owl";

	private static final String GRAPH_VARIABLE_NAME = "graph";

	private static final Logger log = LoggerFactory.getLogger(StaticFileUploader.class);
	
	@Inject
	private SPARQLClientService sparqlClientService;
	@Inject
	private TripleUploadService tripleUploadService;
	
	@Value(value="${spring.triplestore.endpoint.static.query}")
	private String staticQueryEndpoint;
	@Value(value="${spring.triplestore.endpoint.static.update}")
	private String staticUpdateEndpoint;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		checkInit();
		
	}

	private void checkInit() {
		// find graphs
		Query query = new Query();
		query.setQuerySelectType();
		Node graphVar = NodeFactory.createVariable(GRAPH_VARIABLE_NAME);
		ElementNamedGraph namedGraph = new ElementNamedGraph(graphVar, new ElementTriplesBlock());
		query.setQueryPattern(namedGraph);
		query.setDistinct(true);
		query.addResultVar(graphVar);
		
		List<String> graphs = new LinkedList<>(); 
		
		try {
			SPARQLResultSet rs = sparqlClientService.query(this.staticQueryEndpoint, null, query);
			if (rs.getVariableNames().contains(GRAPH_VARIABLE_NAME)) {
				for (Map<String, String> res: rs.getRecords()) {
					graphs.add(res.get(GRAPH_VARIABLE_NAME));
				}
			}
		} catch (Exception e) {
			log.error("Could not query endpoint for graphs",e);
		}
		// insert sequence ontology 
		checkSO(graphs);
		// insert gene ontology
		checkGO(graphs);
	}

	private void checkSO(List<String> graphs) {
		for (String graph: graphs) {
			if (SO_URI.equals(graph)) return;
		}
		log.info("Uploading so.owl into static endpoint");
		UpdateCreate create = new UpdateCreate(NodeFactory.createURI(SO_URI));
		UpdateExecutionFactory.createRemote(create, staticUpdateEndpoint).execute();
		
		InputStream so = this.getClass().getClassLoader().getResourceAsStream("ontologies/so.owl");
		TripleUploader uploader = tripleUploadService.getUploader(staticUpdateEndpoint, SO_URI);
		RDFDataMgr.parse(uploader, so, Lang.RDFXML);
		try {
			so.close();
		} catch (IOException io) {
			log.error("Could not close so.owl");
		}
	}
	
	private void checkGO(List<String> graphs) {
		for (String graph: graphs) {
			if (GO_URI.equals(graph)) return;
		}
		log.info("Uploading go.owl into static endpoint");
		UpdateCreate create = new UpdateCreate(NodeFactory.createURI(GO_URI));
		UpdateExecutionFactory.createRemote(create, staticUpdateEndpoint).execute();
		
		InputStream go = this.getClass().getClassLoader().getResourceAsStream("ontologies/go.owl");
		if (null == go) return;
		TripleUploader uploader = tripleUploadService.getUploader(staticUpdateEndpoint, GO_URI);
		RDFDataMgr.parse(uploader, go, Lang.RDFXML);
		try {
			go.close();
		} catch (IOException io) {
			log.error("Could not close go.owl");
		}
	}

	
	
}
