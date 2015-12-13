package com.genohm.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.genohm.boinq.domain.GenomicRegion;
import com.genohm.boinq.tools.generators.QueryGenerator;
import com.genohm.boinq.web.rest.dto.CriteriaDTO;

@Entity
@DiscriminatorValue(value=CriteriaDTO.FEATURETYPE_CRITERIA)
public class FeatureTypeCriterion extends FeatureSelectCriterion {

    @Column(name="feature_type_uri")
	protected String featureTypeUri;
    @Column(name="feature_type_label")
	protected String featureTypeLabel;
    
    public FeatureTypeCriterion() {
	}
    
	public String getFeatureTypeUri() {
		return featureTypeUri;
	}
	public void setFeatureTypeUri(String featureTypeUri) {
		this.featureTypeUri = featureTypeUri;
	}
	public String getFeatureTypeLabel() {
		return featureTypeLabel;
	}
	public void setFeatureTypeLabel(String featureTypeLabel) {
		this.featureTypeLabel = featureTypeLabel;
	}

	@Override
	public void accept(QueryGenerator qg, GenomicRegion r) {
		qg.visit(this, r);
	}
}