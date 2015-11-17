package com.genohm.boinq.domain.match;

import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;

public class LocationCriteria implements FeatureSelectCriteria {
	protected Long start;
	protected Long end;
	protected String contig;
//	protected String assembly;
	protected Boolean strand;
//	protected Boolean matchStrand = false;

	public LocationCriteria(CriteriaDTO crit) {
		if (crit.type == CriteriaDTO.LOCATION_CRITERIA) {
			this.start = crit.start;
			this.end = crit.end;
			this.contig = crit.contig;
			this.strand = crit.strand;
		}
	}

	@Override
	public void accept(QueryGenerator qg) {
		qg.visit(this);
	}
}
