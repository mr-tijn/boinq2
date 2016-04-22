package com.genohm.boinq.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Track;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDFBase;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.modify.request.QuadDataAcc;
import org.apache.jena.sparql.modify.request.Target;
import org.apache.jena.sparql.modify.request.UpdateCopy;
import org.apache.jena.sparql.modify.request.UpdateDataInsert;
import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.update.UpdateExecutionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TripleUploadService {

	private static Logger log = LoggerFactory.getLogger(TripleUploadService.class);
	
	Set<TripleUploader> currentUploaders = new HashSet<>();
	
	@PostConstruct
	public void init() {
	}
	
	@PreDestroy
	public void destruct() {
		for (TripleUploader uploader: currentUploaders) {
			log.error("Some uploaders were not correctly closed. Use finish().");
			uploader.finish();
		}
	}
		
	public TripleUploader getUploader(String endpoint, String graph, PrefixMapping prefixes) {
		return new TripleUploader(this, endpoint, NodeFactory.createURI(graph), prefixes);
	}
	
	public TripleUploader getUploader(String endpoint, String graphName) {
		return new TripleUploader(this, endpoint, NodeFactory.createURI(graphName), null);
	}
	
	public TripleUploader getUploader(Track track, PrefixMapping prefixes) {
		return new TripleUploader(this, track.getDatasource().getEndpointUpdateUrl(), NodeFactory.createURI(track.getGraphName()), prefixes);
	}
	
	private void register(TripleUploader uploader) {
		currentUploaders.add(uploader);
	}
	
	private void unregister(TripleUploader uploader) {
		currentUploaders.remove(uploader);
	}
	
	public static class TripleUploader extends StreamRDFBase {
		
		//FIXME: prefixes currently unused - check if there is benefit beyond more readable queries
		private static int BATCHSIZE = 1000;
		private Node graph;
		private String endpoint;
		private PrefixMapping prefixes;
		private int count = 0;
		private QuadDataAcc quadData;
		private TripleUploadService uploaderService;
		
		private TripleUploader(TripleUploadService service, String endpoint, Node graph, PrefixMapping prefixes) {
			this.uploaderService = service;
			this.endpoint = endpoint;
			this.graph = graph;
			this.prefixes = prefixes;
			service.register(this);
			init();
		}

		private void init() {
			quadData = new QuadDataAcc();
			quadData.setGraph(graph);
			count = 0;
		}
		
		private void push() {
			UpdateDataInsert insert = new UpdateDataInsert(quadData);
			log.info("UPLOADING " + count + " TRIPLES");
			UpdateExecutionFactory.createRemote(insert, endpoint).execute();
		}
		
		@Override
		public void triple(Triple triple) {
			if (count == BATCHSIZE) { 
				push();
				init();
			}
			quadData.addTriple(triple);
			count++;
		}
		
		@Override
		public void finish() {
			if (count > 0) {
				push();
			}
			uploaderService.unregister(this);
		}
		
	}
	
}


	

