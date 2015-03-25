package com.genohm.boinq.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.faldo.FaldoFeature;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.fileformats.TripleConverter;
import com.genohm.boinq.tools.vocabularies.TrackVocabulary;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;

@Service
public class FaldoService {
	
	private static final Logger log = LoggerFactory.getLogger(FaldoService.class);

	@Inject
	SPARQLClientService sparqlClientService;
	@Inject
	TripleUploadService tripleUploadService;
	@Inject
	QueryBuilderService queryBuilderService;
	
	public List<FaldoFeature> getFeatures(Track track, String refseqName, Long start, Long end, Boolean strand) throws Exception {
		List<FaldoFeature> features = new LinkedList<FaldoFeature>();
		String query = queryBuilderService.findLocalReference(track, NodeFactory.createURI(refseqName));
		SPARQLResultSet resultSet = sparqlClientService.query(track.getDatasource().getMetaEndpointUrl(), track.getDatasource().getMetaGraphName(), query);
		String localReference = refseqName;
		if (resultSet.getRecords().iterator().hasNext()) {
			Map<String, String> match = resultSet.getRecords().iterator().next();
			localReference = match.get(QueryBuilderService.ORIGINAL_REFERENCE);
		}
		if (localReference == null) {
			throw new Exception("Empty local reference for "+refseqName);
		}
		if (resultSet.getRecords().iterator().hasNext()) {
			throw new Exception("No unique reference found for "+refseqName);
		}
		query = queryBuilderService.getFaldoFeatures(localReference, start, end, strand);
		resultSet = sparqlClientService.query(track.getDatasource().getEndpointUrl(), track.getGraphName(), query);
		for (Map<String,String> result: resultSet.getRecords()) {
			FaldoFeature feature = new FaldoFeature();
			feature.id = result.get(QueryBuilderService.FEATURE_ID);
			feature.assembly = result.get(QueryBuilderService.FEATURE_REFERENCE);
			feature.start = Long.parseLong(result.get(QueryBuilderService.FEATURE_BEGIN_POS));
			feature.end = Long.parseLong(result.get(QueryBuilderService.FEATURE_END_POS));
			feature.strand = Boolean.parseBoolean(QueryBuilderService.FEATURE_STRAND);
			features.add(feature);
		}
		return features;
	}
	
	public void writeFeatures(String endpoint, String graph, List<FaldoFeature> features) {
		TripleUploader uploader = tripleUploadService.getUploader(endpoint, graph, QueryBuilderService.faldoPrefixes);
		for (FaldoFeature feature: features) {
			writeFeature(uploader, feature);
		}
	}
	
	public void writeFeatures(Track track, List<FaldoFeature> features) throws Exception {
		writeFeatures(track.getDatasource().getEndpointUpdateUrl(), track.getGraphName(), features);
	}

	private void writeFeature(TripleUploader uploader, FaldoFeature feature) {
		// when writing features ourselves: always use global reference
		List<Triple> triples = TripleConverter.convert(feature);
		for (Triple triple: triples) {
			uploader.put(triple);
		}
	}
	
}
