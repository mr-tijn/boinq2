package com.genohm.boinq.domain.jobs.analysis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.vocabulary.RDF;
import org.crsh.console.jline.internal.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.generators.SPARQLQueryGenerator;

public class SlidingWindowFeatureSelection implements TrackBuildingAnalysis, GenomeAnalysis {

	
	private static Logger log = LoggerFactory.getLogger(SlidingWindowFeatureSelection.class);
	
	@Inject
	TripleUploadService tripleUploadService;
	@Inject
	SPARQLClientService sparqlClient;
	@Inject
	private SPARQLQueryGenerator queryGenerator;

	@Value("${slidingwindow.stepsize}")
	Integer stepSize;
	
	//TODO: get parameters from datasource selected in UI
	private String targetGraph = "test";
	
	private String targetEndpoint;
	@Value("${spring.triplestore.metagraph}")
	private String metaGraph;
	@Value("${spring.triplestore.endpoint.meta.update}")
	private String metaUpdateEndpoint;
	
	private FeatureQuery queryDefinition;
	
	private Boolean interrupt = false;
	private Date startDate;
	private String name;
	private int status = JOB_STATUS_UNKNOWN;
	private Map<Node, Integer> progressPercentages = new HashMap<>();
	
	
	public SlidingWindowFeatureSelection(FeatureQuery queryDefinition) {
		this.queryDefinition = queryDefinition;
		this.name = queryDefinition.getName() + new Date();
	}
	
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
		//TODO: fetch this constant data from metadata 
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
		chromLengths.put(TrackVocab.GRCh37chr01.asNode(), 249250621L);
		chromLengths.put(TrackVocab.GRCh37chr02.asNode(), 243199373L);
		chromLengths.put(TrackVocab.GRCh37chr03.asNode(), 198022430L);
		chromLengths.put(TrackVocab.GRCh37chr04.asNode(), 191154276L);
		chromLengths.put(TrackVocab.GRCh37chr05.asNode(), 180915260L);
		chromLengths.put(TrackVocab.GRCh37chr06.asNode(), 171115067L);
		chromLengths.put(TrackVocab.GRCh37chr07.asNode(), 159138663L);
		chromLengths.put(TrackVocab.GRCh37chr08.asNode(), 146364022L);
		chromLengths.put(TrackVocab.GRCh37chr09.asNode(), 141213431L);
		chromLengths.put(TrackVocab.GRCh37chr10.asNode(), 135534747L);
		chromLengths.put(TrackVocab.GRCh37chr11.asNode(), 135006516L);
		chromLengths.put(TrackVocab.GRCh37chr12.asNode(), 133851895L);
		chromLengths.put(TrackVocab.GRCh37chr13.asNode(), 115169878L);
		chromLengths.put(TrackVocab.GRCh37chr14.asNode(), 107349540L);
		chromLengths.put(TrackVocab.GRCh37chr15.asNode(), 102531392L);
		chromLengths.put(TrackVocab.GRCh37chr16.asNode(), 90354753L);
		chromLengths.put(TrackVocab.GRCh37chr17.asNode(), 81195210L);
		chromLengths.put(TrackVocab.GRCh37chr18.asNode(), 78077248L);
		chromLengths.put(TrackVocab.GRCh37chr19.asNode(), 59128983L);
		chromLengths.put(TrackVocab.GRCh37chr20.asNode(), 63025520L);
		chromLengths.put(TrackVocab.GRCh37chr21.asNode(), 48129895L);
		chromLengths.put(TrackVocab.GRCh37chr22.asNode(), 51304566L);
		chromLengths.put(TrackVocab.GRCh37chrX.asNode(), 155270560L);
		chromLengths.put(TrackVocab.GRCh37chrY.asNode(), 59373566L);
		GenomicRegion region = new GenomicRegion();
		region.strand = null;
		
		this.startDate = new Date();
		
		for (Node chrom: chromosomes) {
			region.assemblyURI = chrom.toString();
			for (Long l = 0L; l <= chromLengths.get(chrom); l += stepSize) {
				region.start = l;
				region.end = l + stepSize - 1;
				progressPercentages.put(chrom, (int) (.5 + 100. * l / chromLengths.get(chrom)));
				// determine query
				Query sparqlQuery = queryGenerator.computeQuery(queryDefinition, region);
				log.debug(sparqlQuery.toString(Syntax.syntaxSPARQL_11));
				// execute it
				try {
					sparqlClient.query(targetEndpoint, targetGraph, sparqlQuery);
				} catch (Exception e) {
//					log.error(messages);
				}
			}
		}
	}
	
	private void writeMetaInfo() {
		TripleUploader metaUploader = tripleUploadService.getUploader(metaUpdateEndpoint, metaGraph, null); 
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
		this.metaUpdateEndpoint = endpoint;
		this.metaGraph = graph;
	}

	@Override
	public int getProgressPercentage(String chromosome) {
		return progressPercentages.get(NodeFactory.createURI(chromosome));
	}

}
