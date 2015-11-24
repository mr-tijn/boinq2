package com.genohm.boinq.domain.jobs.analysis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Value;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;

public class SlidingWindowFeatureSelection implements TrackBuildingAnalysis {

	
	@Inject
	TripleUploadService tripleUploadService;

	@Value("${slidingwindow.stepsize}")
	Integer stepSize;
	
	private String targetGraph;
	private String targetEndpoint;
	private String metaGraph;
	private String metaEndpoint;
	
	private FeatureQuery queryDefinition;
	
	private Boolean interrupt = false;
	private Date startDate;
	private String name;
	private int status = JOB_STATUS_UNKNOWN;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void execute() {
		writeMetaInfo();
		writeData();
	}

	
	private void writeData() {
		Node[] chromosomes = {
				TrackVocab.GRCh37chr01.asNode(),
				TrackVocab.GRCh37chr02.asNode(),
				TrackVocab.GRCh37chr03.asNode(),
				TrackVocab.GRCh37chr04.asNode(),
				TrackVocab.GRCh37chr05.asNode(),
				TrackVocab.GRCh37chr06.asNode(),
				TrackVocab.GRCh37chr07.asNode(),
				TrackVocab.GRCh37chr08.asNode(),
				TrackVocab.GRCh37chr09.asNode(),
				TrackVocab.GRCh37chr10.asNode(),
				TrackVocab.GRCh37chr11.asNode(),
				TrackVocab.GRCh37chr12.asNode(),
				TrackVocab.GRCh37chr13.asNode(),
				TrackVocab.GRCh37chr14.asNode(),
				TrackVocab.GRCh37chr15.asNode(),
				TrackVocab.GRCh37chr16.asNode(),
				TrackVocab.GRCh37chr17.asNode(),
				TrackVocab.GRCh37chr18.asNode(),
				TrackVocab.GRCh37chr19.asNode(),
				TrackVocab.GRCh37chr20.asNode(),
				TrackVocab.GRCh37chr21.asNode(),
				TrackVocab.GRCh37chr22.asNode(),
				TrackVocab.GRCh37chrX.asNode(),
				TrackVocab.GRCh37chrY.asNode(),
				};
		Map<Node, Long> chromLengths = new HashMap<>();
		chromLengths.put(TrackVocab.GRCh37chr01.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr02.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr03.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr04.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr05.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr06.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr07.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr08.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr09.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr10.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr11.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr12.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr13.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr14.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr15.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr16.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr17.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr18.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr19.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr20.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr21.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chr22.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chrX.asNode(), 10001010L);
		chromLengths.put(TrackVocab.GRCh37chrY.asNode(), 10001010L);
		GenomicRegion region = new GenomicRegion();
		region.strand = null;
		for (Node chrom: chromosomes) {
			region.assemblyURI = chrom.toString();
			for (Long l = 0L; l <= chromLengths.get(chrom); l += stepSize) {
				region.start = l;
				region.end = l + stepSize - 1;
				// determine query
				
				// execute it
			}
		}
	}
	
	private void writeMetaInfo() {
		TripleUploader metaUploader = tripleUploadService.getUploader(metaEndpoint, metaGraph, null); 
		Node theGraph = NodeFactory.createURI(targetGraph);
		metaUploader.triple(new Triple(theGraph, RDF.type.asNode(), TrackVocab.FaldoTrack.asNode()));
		metaUploader.finish();
	}
	
	@Override
	public void kill() {
		this.interrupt = true;
	}

	@Override
	public void setTarget(String endpoint, String graph) {
		this.targetEndpoint = endpoint;
		this.targetGraph = graph;
	}

	@Override
	public void setMeta(String endpoint, String graph) {
		this.metaEndpoint = endpoint;
		this.metaGraph = graph;
	}

}
