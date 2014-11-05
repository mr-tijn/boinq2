package com.genohm.boinq.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.modify.request.QuadDataAcc;
import com.hp.hpl.jena.sparql.modify.request.UpdateDataInsert;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

@Service
public class TripleUploadService {

    @Inject
    private Environment env;

	private String sparqlEndpointUri;
	
	@PostConstruct
	public void init() {
		this.sparqlEndpointUri = env.getProperty("spring.tripleupload.endpoint.update");
	}
	
	public void put(Node graphNode, Triple newTriple) {
		QuadDataAcc newData = new QuadDataAcc();
		newData.setGraph(graphNode);
		newData.addTriple(newTriple);
		UpdateDataInsert insertStatement = new UpdateDataInsert(newData);
		UpdateRequest req = new UpdateRequest(insertStatement);
		req.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		//TODO: handle prefixes
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(req, sparqlEndpointUri);
		processor.execute();
	}

}

	

