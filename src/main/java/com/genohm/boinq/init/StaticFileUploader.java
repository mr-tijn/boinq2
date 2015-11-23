package com.genohm.boinq.init;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDFBase;
import org.apache.jena.sparql.modify.request.QuadDataAcc;
import org.apache.jena.sparql.modify.request.UpdateCreate;
import org.apache.jena.sparql.modify.request.UpdateDataInsert;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.update.UpdateExecutionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.service.SPARQLClientService;


@Component
public class StaticFileUploader implements ApplicationListener<ContextRefreshedEvent> {

	private static final String SO_URI = "http://purl.obolibrary.org/obo/so-xp.obo";

	private static final String GRAPH_VARIABLE_NAME = "graph";

	private static final Logger log = LoggerFactory.getLogger(StaticFileUploader.class);
	
	@Inject
	private SPARQLClientService sparqlClientService;
	
	@Value(value="${spring.triplestore.endpoint.static}")
	private String staticEndpoint;
	
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
			SPARQLResultSet rs = sparqlClientService.query(this.staticEndpoint, null, query);
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
	}

	private void checkSO(List<String> graphs) {
		for (String graph: graphs) {
			if (SO_URI.equals(graph)) return;
		}
		log.info("Uploading so.owl into static endpoint");
		UpdateCreate create = new UpdateCreate(NodeFactory.createURI(SO_URI));
		UpdateExecutionFactory.createRemote(create, staticEndpoint).execute();
		
		InputStream so = this.getClass().getClassLoader().getResourceAsStream("ontologies/so.owl");
		RDFDataMgr.parse(new RDFUploader(NodeFactory.createURI(SO_URI), staticEndpoint), so, Lang.RDFXML);
		try {
			so.close();
		} catch (IOException io) {
			log.error("Could not close so.owl");
		}
	}
	
	
	public static class RDFUploader extends StreamRDFBase {
		
		private static int BATCHSIZE = 1000;
		private Node graph;
		private String endpoint;
		private int count = 0;
		private QuadDataAcc quadData = new QuadDataAcc();
		
		public RDFUploader(Node graph, String endpoint) {
			this.graph = graph;
			this.endpoint = endpoint;
			quadData.setGraph(graph);
		}
		
		@Override
		public void triple(Triple triple) {
			if (count == BATCHSIZE) { 
				UpdateDataInsert insert = new UpdateDataInsert(quadData);
				log.info("UPLOADING " + count + " TRIPLES");
				UpdateExecutionFactory.createRemote(insert, endpoint).execute();
				quadData = new QuadDataAcc();
				quadData.setGraph(graph);
				count = 0;
			}
			quadData.addTriple(triple);
			count++;
		}
		
		@Override
		public void finish() {
			if (count > 0) {
				UpdateDataInsert insert = new UpdateDataInsert(quadData);
				log.info("UPLOADING " + count + " TRIPLES");
				UpdateExecutionFactory.createRemote(insert, endpoint).execute();
			}
		}
		
	}
}
