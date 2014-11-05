package com.genohm.boinq.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.Project;

public class ProjectDTO {
    private Long id;
    private Set<DatasourceDTO> datasources;
    private String ownerLogin;
    private String title;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<DatasourceDTO> getDatasources() {
		return datasources;
	}
	public void setDatasourceIds(Set<DatasourceDTO> datasources) {
		this.datasources = datasources;
	}
	public String getOwnerLogin() {
		return ownerLogin;
	}
	public void setOwnerLogin(String ownerLogin) {
		this.ownerLogin = ownerLogin;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ProjectDTO() {}
	
	public ProjectDTO(Long id, Set<DatasourceDTO> datasources, String ownerLogin,
			String title) {
		super();
		this.id = id;
		this.datasources = datasources;
		this.ownerLogin = ownerLogin;
		this.title = title;
	}
    
	public ProjectDTO(Project project) {
		HashSet<DatasourceDTO> datasources = new HashSet<DatasourceDTO>();
		for (Datasource datasource: project.getDatasources()) {
			datasources.add(new DatasourceDTO(datasource));
		}
		this.id = project.getId();
		this.datasources = datasources;
		this.ownerLogin = project.getOwner().getLogin();
		this.title = project.getTitle();
	}
    

}
