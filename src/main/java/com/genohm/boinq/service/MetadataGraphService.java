package com.genohm.boinq.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.jobs.TripleConversion.Metadata;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.repository.RawDataFileRepository;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.fileformats.TripleConverter;
import com.genohm.boinq.tools.fileformats.TripleIteratorFactory;
import com.genohm.boinq.tools.queries.Prefixes;

@Service
public class MetadataGraphService {

	
	@Inject
	private TripleUploadService tripleUploadService;
	@Inject
	private LocalGraphService localGraphService;
	
	

	public void updateFileConversion(String endpoint, String graphIRI, List<Triple> triples) {
		List<Triple> Meta = triples;
		TripleUploader uploader = tripleUploadService.getUploader(endpoint, graphIRI, Prefixes.getCommonPrefixes());
	    while (!Meta.isEmpty()){
			uploader.triple(Meta.get(0));
			Meta.remove(0);
		}
		uploader.finish();
		
	}
	
	public void updateTrackCreation(String graphName, String MetaGraphIRI, String LocalDatasource, String endpoint) {
		List<Triple> Meta = new ArrayList<Triple>();
		Node graphIRI = NodeFactory.createURI(graphName);
		Meta.add(new Triple(graphIRI, RDF.type.asNode(), TrackVocab.Track.asNode()));
		Meta.add(new Triple(NodeFactory.createURI(LocalDatasource), TrackVocab.provides.asNode(), graphIRI));
		TripleUploader uploader = tripleUploadService.getUploader(endpoint, MetaGraphIRI, Prefixes.getCommonPrefixes());
	    while (!Meta.isEmpty()){
			uploader.triple(Meta.get(0));
			Meta.remove(0);
		}
		uploader.finish();

	}
}