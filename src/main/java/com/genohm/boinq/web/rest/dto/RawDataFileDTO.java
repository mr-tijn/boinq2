package com.genohm.boinq.web.rest.dto;

import com.genohm.boinq.domain.RawDataFile;

public class RawDataFileDTO {
    private long id;
    private String filePath;
    private int status;
    private long datasource_id;
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getDatasource_id() {
		return datasource_id;
	}

	public void setDatasource_id(long datasource_id) {
		this.datasource_id = datasource_id;
	}

	public RawDataFileDTO(long id, String filePath, int status,
			Long datasource_id) {
		super();
		this.id = id;
		this.filePath = filePath;
		this.status = status;
		this.datasource_id = datasource_id;
	}
    
	public RawDataFileDTO() {}

	public RawDataFileDTO(RawDataFile rawDataFile) {
		super();
		this.id = rawDataFile.getId();
		this.filePath = rawDataFile.getFilePath();
		this.status = rawDataFile.getStatus();
		this.datasource_id = rawDataFile.getDatasource().getId();
	}

	@Override
	public String toString() {
		return "RawDataFileDTO [id=" + id + ", filePath=" + filePath
				+ ", status=" + status + ", datasource_id=" + datasource_id
				+ "]";
	}
	
	

}
