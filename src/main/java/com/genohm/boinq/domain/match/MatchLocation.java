package com.genohm.boinq.domain.match;

import com.genohm.boinq.tools.generators.SPARQLGenerator;
import com.genohm.boinq.web.rest.dto.MatchDTO;

public class MatchLocation implements Match {

	protected Long start;
	protected Long end;
	protected String contig;
//	protected String assembly;
	protected Boolean strand;
//	protected Boolean matchStrand = false;
	
	public MatchLocation(MatchDTO matchDTO) {
		this.start = matchDTO.start;
		this.end = matchDTO.end;
		this.contig = matchDTO.contig;
		this.strand = matchDTO.strand;
	}
	
	@Override
	public void acceptGenerator(SPARQLGenerator generator, String subjectIdentifier) {
		generator.visitMatch(this, subjectIdentifier);
	}

	@Override
	public MatchDTO asDTO() {
		MatchDTO matchDTO = new MatchDTO();
		matchDTO.type = MatchDTO.MATCH_LOCATION;
		return null;
	}

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

}
