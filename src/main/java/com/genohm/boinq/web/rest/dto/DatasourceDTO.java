package com.genohm.boinq.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.Track;


public class DatasourceDTO {
	private long id;
	private String endpointUrl;
	private String endpointUpdateUrl;
	private String metaEndpointUrl;
	private String metaGraphName;
	private String ownerLogin;
	private Boolean isPublic;
	private String name;
	private String iri;
	private int type;
	private Set<TrackDTO> tracks;
	
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
	public String getMetaEndpointUrl() {
		return metaEndpointUrl;
	}
	public void setMetaEndpointUrl(String metaEndpointUrl) {
		this.metaEndpointUrl = metaEndpointUrl;
	}
	public String getMetaGraphName() {
		return metaGraphName;
	}
	public void setMetaGraphName(String metaGraphName) {
		this.metaGraphName = metaGraphName;
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
	public String getIri() {
		return iri;
	}
	public void setIri(String iri) {
		this.iri = iri;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public Set<TrackDTO> getTracks() {
		return tracks;
	}
	public void setTracks(Set<TrackDTO> tracks) {
		this.tracks = tracks;
	}

	public DatasourceDTO() {}


	public DatasourceDTO(long id, String endpointUrl, String endpointUpdateUrl,
			String metaEndpointUrl, String metaGraphName, String ownerLogin,
			Boolean isPublic, String name, int type,
			Set<TrackDTO> tracks) {
		super();
		this.id = id;
		this.endpointUrl = endpointUrl;
		this.endpointUpdateUrl = endpointUpdateUrl;
		this.metaEndpointUrl = metaEndpointUrl;
		this.metaGraphName = metaGraphName;
		this.ownerLogin = ownerLogin;
		this.isPublic = isPublic;
		this.name = name;
		this.type = type;
		this.tracks = tracks;
	}
	
	public DatasourceDTO(Datasource datasource) {
		this.id = datasource.getId();
		this.endpointUrl = datasource.getEndpointUrl();
		this.endpointUpdateUrl = datasource.getEndpointUpdateUrl();
		this.metaEndpointUrl = datasource.getMetaEndpointUrl();
		this.metaGraphName = datasource.getMetaGraphName();
		this.ownerLogin = datasource.getOwner().getLogin();
		this.isPublic = datasource.getIsPublic();
		this.name = datasource.getName();
		this.type = datasource.getType();
		this.tracks = new HashSet<TrackDTO>();
		for (Track track: datasource.getTracks()) {
			this.tracks.add(new TrackDTO(track));
		}
	}

	@Override
	public String toString() {
		String tracksString = "";
		if (tracks != null) {
			for (TrackDTO track: tracks) {
				tracksString += track;
			}
		}
		return "DatasourceDTO {id=" + id + ", endpointUrl=" + endpointUrl 
				+ ", endpointUpdateUrl=" + endpointUpdateUrl
				+ ", metaEndpointUrl=" + metaEndpointUrl
				+ ", metaGraphName=" + metaGraphName 
				+ ", ownerLogin=" + ownerLogin
				+ ", isPublic=" + isPublic 
				+ ", name=" + name
				+ ", iri=" + iri
				+ ", type=" + type 
				+ ", tracks=[" + tracksString + "]}";
	}

}
