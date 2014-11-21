package com.genohm.boinq.tools.generators;

import com.genohm.boinq.domain.match.MatchAll;
import com.genohm.boinq.domain.match.MatchLocation;

public interface SPARQLGenerator {
//	public void visitMatch(MatchField match, String subjectIdentifier);
	public void visitMatch(MatchAll match, String subjectIdentifier);
//	public void visitMatch(MatchAny match, String subjectIdentifier);
	public void visitMatch(MatchLocation match, String subjectIdentifier);
//	public void visitMatch(MatchType match, String subjectIdentifier);
}

