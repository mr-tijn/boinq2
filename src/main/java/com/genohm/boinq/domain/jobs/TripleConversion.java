package com.genohm.boinq.domain.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.repository.RawDataFileRepository;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.service.MetadataGraphService;
import com.genohm.boinq.service.QueryBuilderService;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.fileformats.TripleIteratorFactory;
import com.genohm.boinq.tools.fileformats.TripleConverter;
import com.genohm.boinq.tools.queries.Prefixes;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.modify.request.QuadDataAcc;

public class TripleConversion implements AsynchronousJob {

	private Boolean interrupted = false;
	@Inject
	private TripleUploadService tripleUploadService;
	@Inject
	private TripleIteratorFactory tripleIteratorFactory;
	@Inject
	private TripleConverter tripleconverter;
	@Inject
	private QueryBuilderService queryBuilder;
	@Inject
	private SPARQLClientService sparqlClient;
	@Inject
	private RawDataFileRepository rawDataFileRepository;
	
	
	private int status = JOB_STATUS_UNKNOWN;
	private String name = "";
	private String description = "";
	
	private RawDataFile inputData;
	
	public static final String SUPPORTED_EXTENSIONS[] = {"GFF", "GFF3", "BED", "VCF"};
	
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
			if (inputData.getStatus() == RawDataFile.STATUS_COMPLETE) {
				throw new Exception("Data is already uploaded");
			}
			// data needed: featureType for the track; referencemapping for the track
			Metadata meta = new Metadata();
			String metagraph = track.getDatasource().getMetaGraphName();
			meta.fileName = inputFile.getPath();
			String endpoint = track.getDatasource().getEndpointUpdateUrl();
			Map<String, Node> referenceMap = getReferenceMap(track);
			Iterator<Triple> tripleIterator = tripleIteratorFactory.getIterator(inputFile, referenceMap, meta);
			TripleUploader uploader = tripleUploadService.getUploader(track, Prefixes.getCommonPrefixes());
			inputData.setStatus(RawDataFile.STATUS_LOADING);
			while (!interrupted && tripleIterator.hasNext()) {
				uploader.triple(tripleIterator.next());
			}
			uploader.finish();
			List<Triple> metadata =tripleconverter.CreateMetadata(meta,track.getGraphName());
			TripleUploader metauploader = tripleUploadService.getUploader(endpoint, metagraph, Prefixes.getCommonPrefixes());
			while (!interrupted && !metadata.isEmpty()) {
				metauploader.triple(metadata.get(0));
				metadata.remove(0);
			}
			metauploader.finish();
			//MetadataGraphService.TrackUpdater(endpoint, metagraph, metadata);
			if (interrupted) throw new Exception("Triple conversion was interrupted by user");
			inputData.setStatus(RawDataFile.STATUS_COMPLETE);
			rawDataFileRepository.save(inputData);
		} catch (Exception e) {
			setStatus(JOB_STATUS_ERROR);
			inputData.setStatus(RawDataFile.STATUS_ERROR);
			rawDataFileRepository.save(inputData);
			this.description = e.getMessage();
			log.error(description);
		}
	}
	
	@Override
	public void kill() {
		this.interrupted = true;
	}
	
	public class Metadata{
		public List<Node> typeList = new ArrayList<Node>();	
		public String fileType = new String();
		public String fileName = new String();
	}
	
}