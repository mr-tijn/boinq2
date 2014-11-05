package com.genohm.boinq.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.RawDataFile;


public class DatasourceDTO {
	private long id;
	private String endpointUrl;
	private String endpointUpdateUrl;
	private String graphName;
	private String ownerLogin;
	private Boolean isPublic;
	private String name;
	private int type;
	private int status;
	private Set<RawDataFileDTO> rawDataFiles;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEndpointUrl() {
		return endpointUrl;
	}
	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
	public String getEndpointUpdateUrl() {
		return endpointUpdateUrl;
	}
	public void setEndpointUpdateUrl(String endpointUpdateUrl) {
		this.endpointUpdateUrl = endpointUpdateUrl;
	}
	public String getGraphName() {
		return graphName;
	}
	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}
	public String getOwnerLogin() {
		return ownerLogin;
	}
	public void setOwnerLogin(String ownerLogin) {
		this.ownerLogin = ownerLogin;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Set<RawDataFileDTO> getRawDataFiles() {
		return rawDataFiles;
	}
	public void setRawDataFiles(Set<RawDataFileDTO> rawDataFiles) {
		this.rawDataFiles = rawDataFiles;
	}

	public DatasourceDTO() {}

	public DatasourceDTO(long id, String endpointUrl, String graphName,
			String ownerLogin, Boolean isPublic, String name, int type,
			int status, Set<RawDataFileDTO> rawDataFiles) {
		super();
		this.id = id;
		this.endpointUrl = endpointUrl;
		this.graphName = graphName;
		this.ownerLogin = ownerLogin;
		this.isPublic = isPublic;
		this.name = name;
		this.type = type;
		this.status = status;
		this.rawDataFiles = rawDataFiles;
	}

	public DatasourceDTO(Datasource datasource) {
		this.id = datasource.getId();
		this.endpointUrl = datasource.getEndpointUrl();
		this.endpointUpdateUrl = datasource.getEndpointUpdateUrl();
		this.graphName = datasource.getGraphName();
		this.ownerLogin = datasource.getOwner().getLogin();
		this.isPublic = datasource.getIsPublic();
		this.name = datasource.getName();
		this.type = datasource.getType();
		this.status = datasource.getStatus();
		this.rawDataFiles = new HashSet<RawDataFileDTO>();
		for (RawDataFile rawDataFile: datasource.getRawDataFiles()) {
			rawDataFiles.add(new RawDataFileDTO(rawDataFile));
		}
	}

	@Override
	public String toString() {
		String rawDataFilesString = "";
		if (rawDataFiles != null) {
			for (RawDataFileDTO rawDataFile: rawDataFiles) {
				rawDataFilesString += rawDataFile;
			}
		}
		return "DatasourceDTO {id=" + id + ", endpointUrl=" + endpointUrl + ", endpointUpdateUrl=" + endpointUpdateUrl
				+ ", graphName=" + graphName + ", ownerLogin=" + ownerLogin
				+ ", isPublic=" + isPublic + ", name=" + name + ", type="
				+ type + ", status=" + status + ", rawDataFiles=[" + rawDataFilesString + "]}";
	}

}
