package org.boinq.domain.query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "T_REFERENCEMAPENTRY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReferenceMapEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	
	@Column(name="boinq_reference_iri", length=255, nullable=true)	
	private String boinqReferenceURI;
	@Column(name="remote_reference_iri", length=255, nullable=true)	
	private String remoteReferenceURI;
	
	public ReferenceMapEntry() {}
	public ReferenceMapEntry(String boinqReference, String remoteReference) {
		this.setBoinqReferenceURI(boinqReference);
		this.setRemoteReferenceURI(remoteReference);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBoinqReferenceURI() {
		return boinqReferenceURI;
	}
	public void setBoinqReferenceURI(String boinqReferenceURI) {
		this.boinqReferenceURI = boinqReferenceURI;
	}
	public String getRemoteReferenceURI() {
		return remoteReferenceURI;
	}
	public void setRemoteReferenceURI(String remoteReferenceURI) {
		this.remoteReferenceURI = remoteReferenceURI;
	}
	
}
