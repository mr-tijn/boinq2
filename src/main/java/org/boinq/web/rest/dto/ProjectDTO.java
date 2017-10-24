package org.boinq.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

import org.boinq.domain.Project;
import org.boinq.domain.Track;

public class ProjectDTO {
    private Long id;
    private Set<TrackDTO> tracks;
    private String ownerLogin;
    private String title;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<TrackDTO> getTracks() {
		return tracks;
	}
	public void setDatasourceIds(Set<TrackDTO> tracks) {
		this.tracks = tracks;
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
	
	public ProjectDTO(Long id, Set<TrackDTO> tracks, String ownerLogin,
			String title) {
		super();
		this.id = id;
		this.tracks = tracks;
		this.ownerLogin = ownerLogin;
		this.title = title;
	}
    
	public ProjectDTO(Project project) {

		HashSet<TrackDTO> tracks = new HashSet<TrackDTO>();
		for (Track track: project.getTracks()) {
			tracks.add(new TrackDTO(track));
		}
		this.id = project.getId();
		this.tracks = tracks;
		this.ownerLogin = project.getOwner().getLogin();
		this.title = project.getTitle();
	}
    

}
