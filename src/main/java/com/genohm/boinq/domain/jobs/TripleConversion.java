package com.genohm.boinq.domain.jobs;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.service.QueryBuilderService;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.fileformats.TripleIteratorFactory;
import com.genohm.boinq.tools.queries.Prefixes;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;

public class TripleConversion implements AsynchronousJob {

	private Boolean interrupted = false;
	@Inject
	private TripleUploadService tripleUploadService;
	@Inject
	private TripleIteratorFactory tripleIteratorFactory;
	@Inject
	private QueryBuilderService queryBuilder;
	@Inject
	private SPARQLClientService sparqlClient;
	
	private int status = JOB_STATUS_UNKNOWN;
	private String name = "";
	private String description = "";
	
	private RawDataFile inputData;
	
	public static final String SUPPORTED_EXTENSIONS[] = {"GFF", "GFF3", "BED"};
	
	private static Logger log = LoggerFactory.getLogger(TripleConversion.class);
	
	public TripleConversion(RawDataFile inputData) {
		// only use setters !
		// some stuff is initialized upon job launch
		this.inputData = inputData;
		this.description = "Triple conversion of "
				+ inputData.getFilePath() + " into track "
				+ inputData.getTrack().getId();
		this.name = this.description;
	}

	@Override
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	

	private Map<String, Node> getReferenceMap(Track track) {
		String endpoint = track.getDatasource().getMetaEndpointUrl();
		String query = queryBuilder.findReferenceMap(track);
		SPARQLResultSet queryResult = null;
		Map<String, Node> resultMap = new HashMap<String, Node>();
		try {
			queryResult = sparqlClient.querySelect(endpoint, track.getGraphName(), query);
		} catch (Exception e) {
			log.error("Could not find reference Map");
			return null;
		}
		for (Map<String,String> record: queryResult.getRecords()) {
			resultMap.put(record.get(QueryBuilderService.ORIGINAL_REFERENCE), NodeFactory.createURI(QueryBuilderService.TARGET_REFERENCE));
		}
		return resultMap;
	}
	
	
	@Override
	public void execute() {
		try {
			Track track = inputData.getTrack();
			if (track == null) {
				throw new Exception("Track is null");
			}
			Datasource datasource = track.getDatasource();
			if (datasource == null) {
				throw new Exception("Datasource is null");				
			}
			if (Datasource.TYPE_LOCAL_FALDO != datasource.getType()) {
				throw new Exception("Datasource should be of type local faldo in order to support upload");
			}
			File inputFile = new File(inputData.getFilePath());
			// data needed: featureType for the track; referencemapping for the track
			Map<String, Node> referenceMap = getReferenceMap(track);
			Iterator<Triple> tripleIterator = tripleIteratorFactory.getIterator(inputFile, referenceMap);
			TripleUploader uploader = tripleUploadService.getUploader(track, Prefixes.getCommonPrefixes());
			inputData.setStatus(RawDataFile.STATUS_LOADING);
			while (!interrupted && tripleIterator.hasNext()) {
				uploader.put(tripleIterator.next());
			}
			if (interrupted) throw new Exception("Triple conversion was interrupted by user");
			inputData.setStatus(RawDataFile.STATUS_COMPLETE);
		} catch (Exception e) {
			setStatus(JOB_STATUS_ERROR);
			inputData.setStatus(RawDataFile.STATUS_ERROR);
			this.description = e.getMessage();
			log.error(description);
		}
	}
	
	@Override
	public void kill() {
		this.interrupted = true;
	}

}
