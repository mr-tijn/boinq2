package com.genohm.boinq.domain.jobs;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.fileformats.BedTripleIterator;
import com.genohm.boinq.tools.fileformats.GFF3TripleIterator;
import com.genohm.boinq.tools.queries.Prefixes;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;

public class TripleConversion implements AsynchronousJob {

	private DatasourceRepository datasourceRepository;
	private TripleUploadService tripleUploadService;
	private int status = JOB_STATUS_UNKNOWN;
	private String name = "";
	private String description = "";
	
	private RawDataFile inputData;
	
	private static final String GFF3_EXTENSIONS[] = {"GFF", "GFF3"};
	private static final String BED_EXTENSIONS[] = {"BED" };
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
	public void setDatasourceRepository(DatasourceRepository datasourceRepository) {
		this.datasourceRepository = datasourceRepository;
	}

	@Override
	public DatasourceRepository getDatasourceRepository() {
		return datasourceRepository;
	}

	@Override
	public TripleUploadService getTripleUploadService() {
		return tripleUploadService;
	}

	@Override
	public void setTripleUploadService(TripleUploadService tripleUploadService) {
		this.tripleUploadService = tripleUploadService;
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
			Iterator<Triple> tripleIterator = getTripleIteratorByExtension(inputFile);
			TripleUploader uploader = tripleUploadService.getUploader(track, Prefixes.getCommonPrefixes());
			inputData.setStatus(RawDataFile.STATUS_LOADING);
			while (tripleIterator.hasNext()) {
				uploader.put(tripleIterator.next());
			}
			inputData.setStatus(RawDataFile.STATUS_COMPLETE);
		} catch (Exception e) {
			setStatus(JOB_STATUS_ERROR);
			inputData.setStatus(RawDataFile.STATUS_ERROR);
			this.description = e.getMessage();
			log.error(description);
		}
	}
	
	private Iterator<Triple> getTripleIteratorByExtension(File inputFile) throws Exception {
		String extension = FilenameUtils.getExtension(inputFile.getName());
		for (String ext: GFF3_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new GFF3TripleIterator(inputFile);
		}
		for (String ext: BED_EXTENSIONS) {
			if (ext.equals(extension.toUpperCase())) return new BedTripleIterator(inputFile);
		}
		throw new Exception("No triple iterator available for extension " + extension);
	}
}
