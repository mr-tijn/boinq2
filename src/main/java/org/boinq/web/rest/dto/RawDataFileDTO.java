package org.boinq.web.rest.dto;

import org.boinq.domain.RawDataFile;

public class RawDataFileDTO {
    private long id;
    private String filePath;
    private int status;
    private long track_id;
    
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

	public long getTrack_id() {
		return track_id;
	}

	public void setTrack_id(long track_id) {
		this.track_id = track_id;
	}

	public RawDataFileDTO(long id, String filePath, int status,
			Long track_id) {
		super();
		this.id = id;
		this.filePath = filePath;
		this.status = status;
		this.track_id = track_id;
	}
    
	public RawDataFileDTO() {}

	public RawDataFileDTO(RawDataFile rawDataFile) {
		super();
		this.id = rawDataFile.getId();
		this.filePath = rawDataFile.getFilePath();
		this.status = rawDataFile.getStatus();
		this.track_id = rawDataFile.getTrack().getId();
	}

	@Override
	public String toString() {
		return "RawDataFileDTO [id=" + id + ", filePath=" + filePath
				+ ", status=" + status + ", track_id=" + track_id
				+ "]";
	}
	
	

}
