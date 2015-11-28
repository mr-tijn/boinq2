package com.genohm.boinq.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.modify.request.QuadDataAcc;
import org.apache.jena.sparql.modify.request.UpdateDataInsert;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.queries.Prefixes;


public class MetadataGraphService {

//	static final private String PREFIX = "spring.triplestore.";
//	
//	static final private String GRAPH_META = "metagraph";
//	private String metaGraph;
//	private RelaxedPropertyResolver propertyResolver;
//	@Override
//	public void setEnvironment(Environment env) {
//		propertyResolver = new RelaxedPropertyResolver(env, PREFIX);
//	}
//	public void init() {
//	this.metaGraph = propertyResolver.getProperty(GRAPH_META);
//	}
	
	@Inject
	private static TripleUploadService tripleUploadService;

	

	public static void TrackUpdater(String endpoint, String graphName, List<Triple> triples) {
		List<Triple> Meta = triples;
		TripleUploader uploader = tripleUploadService.getUploader(endpoint, graphName, Prefixes.getCommonPrefixes());
	    while (!Meta.isEmpty()){
			uploader.triple(Meta.get(0));
			Meta.remove(0);
		}
		uploader.finish();
		
	}
	
	

}