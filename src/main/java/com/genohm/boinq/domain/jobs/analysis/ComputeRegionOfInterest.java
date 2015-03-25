package com.genohm.boinq.domain.jobs.analysis;

import com.genohm.boinq.domain.match.Match;
import com.genohm.boinq.domain.match.MatchFactory;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.tools.generators.ARQGenerator;
import com.genohm.boinq.tools.generators.SPARQLGenerator;
import com.genohm.boinq.web.rest.dto.MatchDTO;

public class ComputeRegionOfInterest implements TrackBuildingAnalysis {

	private LocalGraphService localGraphService;
	private SPARQLClientService sparqlClientService;
	private DatasourceRepository datasourceRepository;
	private TripleUploadService tripleUploadService;
	private String name;
	private String description;
	private MatchDTO matchDTO;
	
	private Boolean irq = false;
	
	private int status = JOB_STATUS_UNKNOWN;
	
	public ComputeRegionOfInterest(MatchDTO matchDTO) {
		this.matchDTO = matchDTO;
	}
	
	
	@Override
	public void setLocalGraphService(LocalGraphService localGraphService) {
		this.localGraphService = localGraphService;
	}

	@Override
	public LocalGraphService getLocalGraphService() {
		return localGraphService;
	}

	@Override
	public void setSPARQLClientService(SPARQLClientService client) {
		this.sparqlClientService = client;
	}

	@Override
	public SPARQLClientService getSPARQLClientService() {
		return sparqlClientService;
	}

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
		// all loops should include check for irq
		this.status = JOB_STATUS_COMPUTING;
		try {
			Match match = MatchFactory.fromDTO(this.matchDTO);
			SPARQLGenerator generator = new ARQGenerator();
			match.acceptGenerator(generator, "brol");
		} catch (Exception e) {
			this.status = JOB_STATUS_ERROR;
		}
	}

	@Override
	public void kill() {
		this.irq = true;
	}

}
