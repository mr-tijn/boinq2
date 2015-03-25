package com.genohm.boinq.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;

public class TrackDTO {

	private long id;
	private int status;
    private String graphName;
    private String name;
    private String type;
    private long datasourceId;
	private Set<RawDataFileDTO> rawDataFiles;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public Set<RawDataFileDTO> getRawDataFiles() {
		return rawDataFiles;
	}

	public void setRawDataFiles(Set<RawDataFileDTO> rawDataFiles) {
		this.rawDataFiles = rawDataFiles;
	}

	public TrackDTO() {}
	
	public TrackDTO(Track track) {
		this.id = track.getId();
		this.status = track.getStatus();
		this.name = track.getName();
		this.graphName = track.getGraphName();
		this.type = track.getType();
		this.rawDataFiles = new HashSet<RawDataFileDTO>();
		for (RawDataFile dataFile: track.getRawDataFiles()) {
			rawDataFiles.add(new RawDataFileDTO(dataFile));
		}
	}
}
