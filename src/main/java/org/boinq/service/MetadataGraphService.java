package org.boinq.service;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.boinq.service.TripleUploadService.TripleUploader;
import org.boinq.tools.queries.Prefixes;
import org.springframework.stereotype.Service;

import org.boinq.generated.vocabularies.TrackVocab;

//TODO: review (martijn)
@Service
public class MetadataGraphService {

	
	@Inject
	private TripleUploadService tripleUploadService;
	
		
	public void updateTrackCreation(String graphName, String metaGraphIRI, String localDatasource, String endpoint) {
		List<Triple> meta = new ArrayList<Triple>();
		Node graphIRI = NodeFactory.createURI(graphName);
		meta.add(new Triple(graphIRI, RDF.type.asNode(), TrackVocab.Track.asNode()));
		meta.add(new Triple(NodeFactory.createURI(localDatasource), TrackVocab.provides.asNode(), graphIRI));
		TripleUploader uploader = tripleUploadService.getUploader(endpoint, metaGraphIRI, Prefixes.getCommonPrefixes());
	    for (Triple triple: meta) {
			uploader.triple(triple);
		}
		uploader.finish();

	}
	
	public void removeTrackInfo(String graphName, String metaGraphIRI, String localDatasource, String endpoint) {
		//TODO: actually remove metadata or add a statement to indicate removal.
	}
	
}