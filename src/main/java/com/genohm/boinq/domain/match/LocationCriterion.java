package com.genohm.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;

@Entity
@DiscriminatorValue(value=CriteriaDTO.LOCATION_CRITERIA)
public class LocationCriterion extends FeatureSelectCriterion {
 
    @Column(name="start")
    protected Long start;

    @Column(name="end")
    protected Long end;
    
    @Column(name="contig")
	protected String contig;
    
    @Column(name="strand")
	protected Boolean strand;

	public LocationCriterion() {}
	
	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public String getContig() {
		return contig;
	}

	public void setContig(String contig) {
		this.contig = contig;
	}

	public Boolean getStrand() {
		return strand;
	}

	public void setStrand(Boolean strand) {
		this.strand = strand;
	}

	@Override
	public void accept(QueryGenerator qg, GenomicRegion r) {
		qg.visit(this, r);
	}
}
