package com.genohm.boinq.web.rest.dto;

import com.genohm.boinq.domain.Datasource;


public class DatasourceDTO {
	private long id;
	private String endpointUrl;
	private String graphName;
	private String ownerLogin;
	private Boolean isPublic;
	private String name;
	private int type;
	private int status;
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
	
	public DatasourceDTO() {}

	public DatasourceDTO(long id, String endpointUrl, String graphName,
			String ownerLogin, Boolean isPublic, String name, int type,
			int status) {
		super();
		this.id = id;
		this.endpointUrl = endpointUrl;
		this.graphName = graphName;
		this.ownerLogin = ownerLogin;
		this.isPublic = isPublic;
		this.name = name;
		this.type = type;
		this.status = status;
	}

	public DatasourceDTO(Datasource datasource) {
		this.id = datasource.getId();
		this.endpointUrl = datasource.getEndpointUrl();
		this.graphName = datasource.getGraphName();
		this.ownerLogin = datasource.getOwner().getLogin();
		this.isPublic = datasource.getIsPublic();
		this.name = datasource.getName();
		this.type = datasource.getType();
		this.status = datasource.getStatus();
	}

	@Override
	public String toString() {
		return "DatasourceDTO {id=" + id + ", endpointUrl=" + endpointUrl
				+ ", graphName=" + graphName + ", ownerLogin=" + ownerLogin
				+ ", isPublic=" + isPublic + ", name=" + name + ", type="
				+ type + ", status=" + status + "}";
	}

}
