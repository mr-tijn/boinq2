package com.genohm.boinq.domain.jobs.analysis;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.jena.graph.Triple;

import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.faldo.FaldoFeature;
import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.service.FaldoFeatureBuilder;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.service.QueryBuilderService;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.fileformats.TripleConverter;
import com.genohm.boinq.web.rest.dto.MatchDTO;

public class ComputeRegionOfInterest implements TrackBuildingAnalysis {

	@Inject
	private LocalGraphService localGraphService;
	@Inject
	private QueryBuilderService queryBuilderService;
	@Inject
	private SPARQLClientService sparqlClientService;
	@Inject
	private TrackRepository trackRepository;
	@Inject
	private TripleConverter tripleConverter;
	@Inject
	private FaldoFeatureBuilder faldoBuilder;
	@Inject
	private TripleUploadService tripleUploadService;
	
	private String name;
	private String description;
	private MatchDTO matchDTO;
	private Track track;
	private TripleUploader tripleUploader;
	
	private int status = JOB_STATUS_UNKNOWN;
	private Boolean irq = false;
	
	public ComputeRegionOfInterest(MatchDTO matchDTO, Long sourceTrackId) {
		this.matchDTO = matchDTO;
		this.track = trackRepository.findOne(sourceTrackId);
		this.tripleUploader = tripleUploadService.getUploader(track, null);
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
			String query = queryBuilderService.getQueryFromMatch(matchDTO);
			SPARQLResultSet srs = sparqlClientService.querySelect(track.getDatasource().getEndpointUrl(), track.getGraphName(), query);
			if (! srs.getRecords().isEmpty()) {
				for (Map<String,String> record : srs.getRecords()) {
					if (killed()) return;
					FaldoFeature feature = faldoBuilder.fromResultRecord(record);
					List<Triple> resultTriples = tripleConverter.convert(feature);
					for (Triple triple: resultTriples) {
						if (killed()) return;
						tripleUploader.triple(triple);
					}
					tripleUploader.finish();
				}
			}
		} catch (Exception e) {
			this.status = JOB_STATUS_ERROR;
		}
	}
	
	@Override
	public void kill() {
		this.irq = false;
	}

	private Boolean killed() {
		return this.irq;
	}

	@Override
	public void setTarget(String endpoint, String graph) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMeta(String endpoint, String graph) {
		// TODO Auto-generated method stub
		
	}

}
