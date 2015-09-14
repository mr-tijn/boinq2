package com.genohm.boinq.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Track;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.modify.request.QuadDataAcc;
import org.apache.jena.sparql.modify.request.UpdateDataInsert;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

@Service
public class TripleUploadService {

	@PostConstruct
	public void init() {
	}
		
	public TripleUploader getUploader(String endpoint, String graph, PrefixMapping prefixes) {
		return new TripleUploader(endpoint, graph, prefixes);
	}
	
	public TripleUploader getUploader(Track track, PrefixMapping prefixes) {
		return new TripleUploader(track.getDatasource().getEndpointUpdateUrl(), track.getGraphName(), prefixes);
	}
	
	public class TripleUploader {
		private String sparqlEndpointUri;
		private PrefixMapping prefixes;
		private Node graphNode;
		
		public TripleUploader(String endpoint, String graph, PrefixMapping prefixes) {
			this.prefixes = prefixes;
			this.sparqlEndpointUri = endpoint;
			this.graphNode = NodeFactory.createURI(graph);
		}
		
		public void put(Triple newTriple) {
			QuadDataAcc newData = new QuadDataAcc();
			newData.setGraph(graphNode);
			newData.addTriple(newTriple);
			UpdateDataInsert insertStatement = new UpdateDataInsert(newData);
			UpdateRequest req = new UpdateRequest(insertStatement);
			req.setPrefixMapping(prefixes);
			UpdateProcessor processor = UpdateExecutionFactory.createRemote(req, sparqlEndpointUri);
			processor.execute();
			
		}
	}
}


	

