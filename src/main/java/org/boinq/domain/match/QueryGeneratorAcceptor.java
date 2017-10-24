package org.boinq.domain.match;

import org.boinq.domain.GenomicRegion;
import org.boinq.tools.generators.QueryGenerator;

public interface QueryGeneratorAcceptor {
	void accept(QueryGenerator qg, GenomicRegion region);
	Boolean check(QueryGenerator qg, GenomicRegion region);
}
