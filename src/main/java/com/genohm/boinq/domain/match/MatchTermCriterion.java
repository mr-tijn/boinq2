package com.genohm.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;

@Entity
@DiscriminatorValue(value=CriteriaDTO.MATCHTERM_CRITERIA)
public class MatchTermCriterion extends MatchFieldCriterion {

	// operator properties
	@Transient
	private String termSourceEndpoint; // track:endpointUrl
	@Transient
	private String termSourceGraph; // track:graphUri
	@Transient
	private String termSourceRoot; // track:rootTerm
	
	// criterion properties
    @Column(name="term_uri")
	private String termUri;
    @Column(name="term_label")
	private String termLabel;
    
    
	public String getTermSourceEndpoint() {
		return termSourceEndpoint;
	}
	public void setTermSourceEndpoint(String termSourceEndpoint) {
		this.termSourceEndpoint = termSourceEndpoint;
	}
	public String getTermSourceGraph() {
		return termSourceGraph;
	}
	public void setTermSourceGraph(String termSourceGraph) {
		this.termSourceGraph = termSourceGraph;
	}
	public String getTermSourceRoot() {
		return termSourceRoot;
	}
	public void setTermSourceRoot(String termSourceRoot) {
		this.termSourceRoot = termSourceRoot;
	}
	public String getTermUri() {
		return termUri;
	}
	public void setTermUri(String termUri) {
		this.termUri = termUri;
	}
	public String getTermLabel() {
		return termLabel;
	}
	public void setTermLabel(String termLabel) {
		this.termLabel = termLabel;
	}
	
	@Override
	public void accept(QueryGenerator qg, GenomicRegion region) {
		qg.visit(this, region);
	}
	
	@Override
	public Boolean check(QueryGenerator qg, GenomicRegion region) {
		return qg.check(this, region);
	}

    
    
}
