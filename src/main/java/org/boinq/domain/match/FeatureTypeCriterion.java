package org.boinq.domain.match;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.boinq.domain.GenomicRegion;
import org.boinq.tools.generators.QueryGenerator;
import org.boinq.web.rest.dto.CriteriaDTO;

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
	public Boolean check(QueryGenerator qg, GenomicRegion region) {
		return qg.check(this, region);
	}

	@Override
	public void accept(QueryGenerator qg, GenomicRegion r) {
		qg.visit(this, r);
	}
	
	@Override
	public CriteriaDTO createDTO() {
		CriteriaDTO result = new CriteriaDTO();
		result.featureTypeUri = this.featureTypeUri;
		result.featureTypeLabel = this.featureTypeLabel;
		result.type = CriteriaDTO.FEATURETYPE_CRITERIA;
		return result;
	}
}