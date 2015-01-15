package com.genohm.boinq.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.tools.queries.Prefixes;
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

    @Inject
    private Environment env;

	private String defaultSparqlEndpointUri;
	
	@PostConstruct
	public void init() {
		this.defaultSparqlEndpointUri = env.getProperty("spring.tripleupload.endpoint.update");
	}
		
	public TripleUploader getUploader(Track track, PrefixMapping prefixes) {
		return new TripleUploader(track, prefixes);
	}

	public class TripleUploader {
		private String sparqlEndpointUri;
		private PrefixMapping prefixes;
		private Node graphNode;
		
		public TripleUploader(Track track, PrefixMapping prefixes) {
			this.prefixes = prefixes;
			this.sparqlEndpointUri = track.getDatasource().getEndpointUpdateUrl();
			this.graphNode = NodeFactory.createURI(track.getGraphName());
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


	

