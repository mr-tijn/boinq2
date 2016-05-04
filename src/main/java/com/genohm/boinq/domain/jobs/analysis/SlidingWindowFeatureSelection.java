package com.genohm.boinq.domain.jobs.analysis;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Transient;

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
import com.genohm.boinq.repository.AnalysisRepository;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.service.QueryBuilderService;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.generators.SPARQLQueryGenerator;
import com.genohm.boinq.web.rest.dto.AnalysisDTO;

@Entity
public class SlidingWindowFeatureSelection extends GenomeAnalysis {

	
	private static Logger log = LoggerFactory.getLogger(SlidingWindowFeatureSelection.class);
	
	@Transient
	@Inject
	TripleUploadService tripleUploadService;
	@Transient
	@Inject
	SPARQLClientService sparqlClient;
	@Transient
	@Inject
	LocalGraphService localGraphService;
	@Transient
	@Inject
	QueryBuilderService queryBuilderService;
	// TODO: should be a new generator each time
	@Transient
	@Inject
	private SPARQLQueryGenerator queryGenerator;
	@Transient
	@Inject
	private AnalysisRepository analysisRepository;

	@Transient
	@Value("${slidingwindow.stepsize}")
	Integer stepSize;
	@Transient
	@Value("${spring.triplestore.metagraph}")
	private String metaGraph;
	@Transient
	@Value("${spring.triplestore.endpoint.meta.update}")
	private String metaUpdateEndpoint;
	@Transient
	@Value("${spring.triplestore.endpoint.meta.query}")
	private String metaQueryEndpoint;
	@Transient
	@Value("${spring.triplestore.endpoint.data.update}")
	private String dataUpdateEndpoint;
	
	@Transient
	private FeatureQuery queryDefinition;
	
	@Transient
	private Boolean interrupt = false;
	@Transient
	private Date startDate;
	@Transient
	private Map<Node, Long> referenceLengths = new HashMap<>();
	@Transient
	private List<Node> references = new LinkedList<>();

	
	public SlidingWindowFeatureSelection() {}
	
	public SlidingWindowFeatureSelection(FeatureQuery queryDefinition) {
		this.queryDefinition = queryDefinition;
		setName(queryDefinition.getName() + new Date());
	}
	
	
	@Override
	public void execute() {
		this.setStatus(JOB_STATUS_COMPUTING);
		queryDefinition.setTargetGraph(localGraphService.createLocalGraph(queryDefinition.getTargetGraph()));
		try {
			save();
			init();
			writeMetaInfo();
			writeData();
			this.setStatus(JOB_STATUS_SUCCESS);
			save();
		} catch (Exception e) {
			this.setStatus(JOB_STATUS_ERROR);
			this.errorDescription = e.getMessage();
			log.error(errorDescription);
			try {
				save();
			} catch (Exception ee) {
				log.error("Could not save analysis ",ee);
			}
		}
	}
	
	private void save() throws Exception {
		try {
			analysisRepository.save(this);
		} catch (Exception e) {
			throw new Exception("Could not save job state for job " + getName(), e);
		}
	}
	
	private void init() throws Exception {
		
		if (queryDefinition.getReferenceAssemblyUri() == null) {
			throw new Exception("No reference assembly given");
		}
		String referenceQuery = queryBuilderService.getReferencesForAssembly(queryDefinition.getReferenceAssemblyUri());
		SPARQLResultSet resultSet = sparqlClient.querySelect(metaQueryEndpoint, metaGraph, referenceQuery);
		for (Map<String, String> record: resultSet.getRecords()) {
			String refString = record.get(QueryBuilderService.URI);
			String lengthString = record.get(QueryBuilderService.LENGTH);
			Node reference = NodeFactory.createURI(refString);
			references.add(reference);
			referenceLengths.put(reference,Long.parseLong(lengthString));
		}
		queryGenerator.setReferences(references);
		
		this.startDate = new Date();
		for (Node chrom : references) {
			progressPercentages.put(chrom.getURI(), 0.);
		}
		
		save();
	}
	
	private void writeData() throws Exception {
		
		
		GenomicRegion region = new GenomicRegion();
		region.strand = null;
		
		for (Node chrom: references) {
			region.assemblyURI = chrom.toString();
			for (Long l = 0L; l <= referenceLengths.get(chrom); l += stepSize) {
				if (interrupt) return;
				region.start = l;
				region.end = l + stepSize - 1;
				progressPercentages.put(chrom.getURI(), 100. * l / referenceLengths.get(chrom));
				save();
				Update sparqlQuery = queryGenerator.computeUpdate(queryDefinition, region);
				try {
					UpdateExecutionFactory.createRemote(sparqlQuery, dataUpdateEndpoint).execute();
				} catch (Exception e) {
					throw new Exception("Could not perform query " + sparqlQuery, e);
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
	public AnalysisDTO createDTO() {
		AnalysisDTO result = super.createDTO();
		result.type = AnalysisDTO.TYPE_FEATURESELECTION;
		return result;
	}


}
