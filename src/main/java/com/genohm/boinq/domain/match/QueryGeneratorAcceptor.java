package com.genohm.boinq.domain.match;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;

public interface QueryGeneratorAcceptor {
	void accept(QueryGenerator qg, GenomicRegion region);
	Boolean check(QueryGenerator qg, GenomicRegion region);
}
