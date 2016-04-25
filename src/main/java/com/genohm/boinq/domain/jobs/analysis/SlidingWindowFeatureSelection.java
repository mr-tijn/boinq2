package com.genohm.boinq.domain.jobs.analysis;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.vocabulary.RDF;
import org.crsh.console.jline.internal.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.domain.match.FeatureQuery;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.service.QueryBuilderService;
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
	LocalGraphService localGraphService;
	@Inject
	QueryBuilderService queryBuilderService;
	// TODO: should be a new generator each time
	@Inject
	private SPARQLQueryGenerator queryGenerator;

	@Value("${slidingwindow.stepsize}")
	Integer stepSize;
	
	
	@Value("${spring.triplestore.metagraph}")
	private String metaGraph;
	@Value("${spring.triplestore.endpoint.meta.update}")
	private String metaUpdateEndpoint;
	@Value("${spring.triplestore.endpoint.meta.query}")
	private String metaQueryEndpoint;
	@Value("${spring.triplestore.endpoint.data.update}")
	private String dataUpdateEndpoint;
	
	private FeatureQuery queryDefinition;
	
	private Boolean interrupt = false;
	private Date startDate;
	private String name;
	private String errorDescription;
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
	
	private void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getErrorDescription() {
		return errorDescription;
	}
	
	@Override
	public void execute() {
		this.setStatus(JOB_STATUS_COMPUTING);
		queryDefinition.setTargetGraph(localGraphService.createLocalGraph(queryDefinition.getTargetGraph()));
		try {
			writeMetaInfo();
			writeData();
		} catch (Exception e) {
			this.setStatus(JOB_STATUS_ERROR);
			this.errorDescription = e.getMessage();
			log.error(errorDescription);
		}
	}
	
	private void writeData() throws Exception {
		
		if (queryDefinition.getReferenceAssemblyUri() == null) {
			throw new Exception("No reference assembly given");
		}
		String referenceQuery = queryBuilderService.getReferencesForAssembly(queryDefinition.getReferenceAssemblyUri());
		List<Node> references = new LinkedList<>();
		Map<Node, Long> referenceLengths = new HashMap<>();
		SPARQLResultSet resultSet = sparqlClient.querySelect(metaQueryEndpoint, metaGraph, referenceQuery);
		for (Map<String, String> record: resultSet.getRecords()) {
			String refString = record.get(QueryBuilderService.URI);
			String lengthString = record.get(QueryBuilderService.LENGTH);
			Node reference = NodeFactory.createURI(refString);
			references.add(reference);
			referenceLengths.put(reference,Long.parseLong(lengthString));
		}
		queryGenerator.setReferences(references);
		
		GenomicRegion region = new GenomicRegion();
		region.strand = null;
		
		this.startDate = new Date();
		
		for (Node chrom: references) {
			region.assemblyURI = chrom.toString();
			for (Long l = 0L; l <= referenceLengths.get(chrom); l += stepSize) {
				if (interrupt) return;
				region.start = l;
				region.end = l + stepSize - 1;
				progressPercentages.put(chrom, (int) (.5 + 100. * l / referenceLengths.get(chrom)));
				Update sparqlQuery = queryGenerator.computeUpdate(queryDefinition, region);
				try {
					UpdateExecutionFactory.createRemote(sparqlQuery, dataUpdateEndpoint).execute();
				} catch (Exception e) {
//					log.error(messages);
				}
			}
		}
	}
	
	private void writeMetaInfo() {
		TripleUploader metaUploader = tripleUploadService.getUploader(metaUpdateEndpoint, metaGraph, null); 
		Node theGraph = NodeFactory.createURI(queryDefinition.getTargetGraph());
		metaUploader.triple(new Triple(theGraph, RDF.type.asNode(), TrackVocab.FaldoTrack.asNode()));
		metaUploader.finish();
	}
	
	@Override
	public void kill() {
		this.interrupt = true;
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
