package com.genohm.boinq.service;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.modify.request.QuadDataAcc;
import org.apache.jena.sparql.modify.request.UpdateDataDelete;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Service;

import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.repository.RawDataFileRepository;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.queries.Prefixes;

//TODO: review (martijn)
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
	
	public void updateTrackCreation(String graphName, String metaGraphIRI, String localDatasource, String endpoint) {
		List<Triple> Meta = new ArrayList<Triple>();
		Node graphIRI = NodeFactory.createURI(graphName);
		Meta.add(new Triple(graphIRI, RDF.type.asNode(), TrackVocab.Track.asNode()));
		Meta.add(new Triple(NodeFactory.createURI(localDatasource), TrackVocab.provides.asNode(), graphIRI));
		TripleUploader uploader = tripleUploadService.getUploader(endpoint, metaGraphIRI, Prefixes.getCommonPrefixes());
	    while (!Meta.isEmpty()){
			uploader.triple(Meta.get(0));
			Meta.remove(0);
		}
		uploader.finish();

	}
	
	public void removeTrackInfo(String graphName, String metaGraphIRI, String localDatasource, String endpoint) {
		//TODO: actually remove metadata or add a statement to indicate removal.
	}
	
}