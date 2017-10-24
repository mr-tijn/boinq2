package org.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public abstract class MatchFieldCriterion extends FeatureSelectCriterion {
	
	// operator properties
	@Column(name="match_name")
	private String matchName; // track:matchName
    @Column(name="path_expression")
	private String pathExpression; // track:pathExpression

    // criterion properties
    @Column(name="exact_match")
	private Boolean exactMatch; 
	
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public String getPathExpression() {
		return pathExpression;
	}
	public void setPathExpression(String pathExpression) {
		this.pathExpression = pathExpression;
	}
	public Boolean getExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(Boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	
	
}
