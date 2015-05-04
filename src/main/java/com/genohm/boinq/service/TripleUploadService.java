package com.genohm.boinq.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Track;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

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


	

