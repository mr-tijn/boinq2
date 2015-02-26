package com.genohm.boinq.domain.jobs.analysis;

import com.genohm.boinq.domain.Track;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;

public class CountReads implements Analysis {

	private DatasourceRepository datasourceRepository;
	private TripleUploadService tripleUploadService;
	private String name;
	private int status;
	private String description;
	private SPARQLClientService sparqlClientService;
	
	private Track roiTrack;
	
	
	@Override
	public void setDatasourceRepository(
			DatasourceRepository datasourceRepository) {
		this.datasourceRepository = datasourceRepository;
	}

	@Override
	public DatasourceRepository getDatasourceRepository() {
		return datasourceRepository;
	}

	@Override
	public void setTripleUploadService(TripleUploadService tripleUploadService) {
		this.tripleUploadService = tripleUploadService;
	}

	@Override
	public TripleUploadService getTripleUploadService() {
		return tripleUploadService;
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
		return description;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSPARQLClientService(SPARQLClientService client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SPARQLClientService getSPARQLClientService() {
		return sparqlClientService;
		
	}

}
